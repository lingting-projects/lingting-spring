package live.lingting.spring.redis

import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.function.Function
import live.lingting.framework.lock.SpinLock
import live.lingting.framework.time.DateTime
import live.lingting.framework.value.WaitValue
import live.lingting.spring.redis.cache.RedisCache
import live.lingting.spring.redis.lock.RedisLock
import live.lingting.spring.redis.lock.RedisLockParams
import live.lingting.spring.redis.properties.RedisProperties
import live.lingting.spring.redis.script.RedisScriptExecutor
import live.lingting.spring.redis.script.RepeatRedisScript
import org.springframework.beans.factory.InitializingBean
import org.springframework.data.redis.connection.RedisScriptingCommands
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.StreamOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.data.redis.serializer.RedisSerializer

/**
 * @author lingting 2024-04-17 14:29
 */
@Suppress("UNCHECKED_CAST")
class Redis(
    private val template: StringRedisTemplate,
    private val properties: RedisProperties,
) : InitializingBean {

    companion object {
        private val INSTANCE = WaitValue<Redis>()

        @JvmStatic
        fun instance(): Redis {
            return INSTANCE.notNull()
        }
    }

    fun template(): StringRedisTemplate {
        return template
    }

    fun keySerializer(): RedisSerializer<String> {
        return template.keySerializer as RedisSerializer<String>
    }

    fun valueSerializer(): RedisSerializer<String> {
        return template.valueSerializer as RedisSerializer<String>
    }

    fun stringSerializer(): RedisSerializer<String> {
        return template.stringSerializer
    }

    fun hashOps(): HashOperations<String, String, String> {
        return template.opsForHash<String, String>()
    }

    fun valueOps(): ValueOperations<String, String> {
        return template.opsForValue()
    }

    fun listOps(): ListOperations<String, String> {
        return template.opsForList()
    }

    fun setOps(): SetOperations<String, String> {
        return template.opsForSet()
    }

    fun zSetOps(): ZSetOperations<String, String> {
        return template.opsForZSet()
    }

    fun streamOps(): StreamOperations<String, String, String> {
        return template.opsForStream<String, String>()
    }

    @JvmOverloads
    fun cache(key: String, expireTime: Duration? = properties.cacheExpireTime, lockTimeout: Duration = properties.lockTimeout, leaseTime: Duration? = properties.leaseTime): RedisCache {
        return RedisCache(key, this, properties.nullValue, expireTime, lockTimeout, leaseTime)
    }

    override fun afterPropertiesSet() {
        INSTANCE.update(this)
    }

    // region script

    fun <T> script(script: RedisScript<T>): RedisScriptExecutor<T> {
        val repeat = RepeatRedisScript.of(script)
        return RedisScriptExecutor(repeat, template)
    }

    fun <T> script(script: RepeatRedisScript<T>): RedisScriptExecutor<T> {
        return RedisScriptExecutor(script, template)
    }

    fun <T> execute(function: Function<RedisScriptingCommands, T>) {
        return template.execute {
            val commands = it.scriptingCommands()
            function.apply(commands)
        }
    }

    fun load(script: RepeatRedisScript<*>) {
        val executor = script(script)
        executor.load()
    }

    fun load(scripts: Collection<RepeatRedisScript<*>>) {
        execute {
            var executor: RedisScriptExecutor<*>? = null
            scripts.filter { !it.load }.forEach { script ->
                if (executor == null) {
                    executor = script(script)
                }
                executor = executor.copy(script)
                executor.load(it)
            }
        }
    }

    @JvmOverloads
    fun lock(key: String, params: RedisLockParams = RedisLockParams.DEFAULT): RedisLock {
        return RedisLock(key, params, this)
    }

    @JvmOverloads
    fun spin(key: String, params: RedisLockParams = RedisLockParams.DEFAULT): SpinLock {
        return RedisLock.spin(key, params, this)
    }

    // endregion

    // region key
    /**
     * 删除指定的 key
     * @param key 要删除的 key
     * @return 删除成功返回 true, 如果 key 不存在则返回 false
     * @see [Del Command](http://redis.io/commands/del)
     */
    fun delete(key: String): Boolean {
        return true == template().delete(key)
    }

    /**
     * 删除指定的 keys
     * @param keys 要删除的 key 数组
     * @return 如果删除了一个或多个 key，则为大于 0 的整数，如果指定的 key 都不存在，则为 0
     */
    fun delete(vararg keys: String): Long {
        return delete(keys.toList())
    }

    fun delete(keys: Collection<String>): Long {
        val l = template().delete(keys)
        return l ?: 0
    }

    /**
     * 判断 key 是否存在
     * @param key 待判断的 key
     * @return 如果 key 存在 `true` , 否则返回 `false`
     * @see [Exists Command](http://redis.io/commands/exists)
     */
    fun exists(key: String): Boolean {
        return true == template().hasKey(key)
    }

    /**
     * 判断指定的 key 是否存在.
     * @param keys 待判断的数组
     * @return 指定的 keys 在 redis 中存在的的数量
     * @see [Exists Command](http://redis.io/commands/exists)
     */
    fun exists(vararg keys: String): Long {
        return exists(keys.toList())
    }

    fun exists(keys: Collection<String>): Long {
        val l = template().countExistingKeys(keys)
        return l ?: 0
    }

    /**
     * 设置过期时间
     * @param key 待修改过期时间的 key
     * @param timeout 时长
     * @param timeUnit 时间单位
     */
    fun expire(key: String, timeout: Duration): Boolean {
        return expire(key, timeout.toSeconds(), TimeUnit.SECONDS)
    }

    /**
     * 设置过期时间
     * @param key 待修改过期时间的 key
     * @param timeout 过期时长，单位 秒
     * @see [Expire Command](http://redis.io/commands/expire)
     */
    @JvmOverloads
    fun expire(key: String, timeout: Long, timeUnit: TimeUnit = TimeUnit.SECONDS): Boolean {
        return true == template().expire(key, timeout, timeUnit)
    }

    /**
     * 设置 key 的过期时间到指定的日期
     * @param key 待修改过期时间的 key
     * @param date 过期时间
     * @return 修改成功返回 true
     * @see [ExpireAt Command](https://redis.io/commands/expireat/)
     */
    fun expireAt(key: String, date: Date): Boolean {
        return true == template().expireAt(key, date)
    }

    fun expireAt(key: String, expireAt: Instant): Boolean {
        return true == template().expireAt(key, expireAt)
    }

    /**
     * 获取所有符合指定表达式的 key
     * @param pattern 表达式
     * @return java.util.Set<java.lang.String>
     * @see [Keys Command](http://redis.io/commands/keys)
    </java.lang.String> */
    fun keys(pattern: String): MutableSet<String> {
        return template().keys(pattern)
    }

    /**
     * TTL 命令返回 [EXPIRE][this.expire] 命令设置的剩余生存时间（以秒为单位）.。
     * 时间复杂度: O(1)
     * @param key 待查询的 key
     * @return TTL 以秒为单位，或负值以指示错误
     * @see [TTL Command](http://redis.io/commands/ttl)
     */
    fun ttl(key: String): Long {
        val l = template().getExpire(key)
        return l ?: 0
    }

    /**
     * 使用 Cursor 遍历指定规则的 keys
     * @param scanOptions scan 的配置
     * @return Cursor，一个可迭代对象
     * @see [Scan Command](https://redis.io/commands/scan/)
     */
    fun scan(scanOptions: ScanOptions): Cursor<String> {
        return template().scan(scanOptions)
    }

    /**
     * 使用 Cursor 遍历指定规则的 keys
     * @param patten key 的规则
     * @return Cursor，一个可迭代对象
     */
    fun scan(patten: String): Cursor<String> {
        val scanOptions = ScanOptions.scanOptions().match(patten).build()
        return scan(scanOptions)
    }

    /**
     * 使用 Cursor 遍历指定规则的 keys
     * @param patten key 的规则
     * @param count 一次扫描获取的 key 数量， 默认为 10
     * @return Cursor，一个可迭代对象
     * @see [Scan Command](https://redis.io/commands/scan/)
     */
    fun scan(patten: String, count: Long): Cursor<String> {
        val scanOptions = ScanOptions.scanOptions().match(patten).count(count).build()
        return scan(scanOptions)
    }

    // endregion

    // region string
    /**
     * 当 key 存在时，对其值进行自减操作 （自减步长为 1），当 key 不存在时，则先赋值为 0 再进行自减
     * @param key key
     * @return 自减之后的 value 值
     * @see this.decrement
     */
    fun decrement(key: String): Long {
        val l = valueOps().decrement(key)
        return l ?: 0
    }

    /**
     * 当 key 存在时，对其值进行自减操作，当 key 不存在时，则先赋值为 0 再进行自减
     * @param key key
     * @param delta 自减步长
     * @return 自减之后的 value 值
     * @see [DecrBy Command](http://redis.io/commands/decrby)
     */
    fun decrement(key: String, delta: Long): Long {
        val l = valueOps().decrement(key, delta)
        return if (l == null) 0 else l
    }

    /**
     * 获取指定 key 的 value 值
     * @param key 指定的 key
     * @return 当 key 不存在时返回 null
     * @see [Get Command](http://redis.io/commands/get)
     */
    fun get(key: String): String? {
        return valueOps().get(key)
    }

    /**
     * 获取指定 key 的 value 值，并将指定的 key 进行删除
     * @param key 指定的 key
     * @return 当 key 不存在时返回 null
     * @see [GetDel Command](http://redis.io/commands/getdel/)
     */
    fun getAndDelete(key: String): String? {
        return valueOps().getAndDelete(key)
    }

    /**
     * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
     * @param key 指定的 key
     * @param timeout 过期时间，单位时间秒
     * @return 当 key 不存在时返回 null
     * @see [GetEx Command](http://redis.io/commands/getex/)
     */
    fun getAndExpire(key: String, timeout: Long): String? {
        return getAndExpire(key, timeout, TimeUnit.SECONDS)
    }

    /**
     * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
     * @param key 指定的 key
     * @param timeout 过期时间，单位时间秒
     * @param timeUnit 时间单位
     * @return 当 key 不存在时返回 null
     * @see [GetEx Command](http://redis.io/commands/getex/)
     */
    fun getAndExpire(key: String, timeout: Long, timeUnit: TimeUnit): String? {
        return valueOps().getAndExpire(key, timeout, timeUnit)
    }

    /**
     * 获取指定的 key 的 value 值，并同时使用指定的 value 值进行覆盖操作
     * @param key 指定的 key
     * @param value 新的 value 值
     * @return 当 key 存在时返回其 value 值，否则返回 null
     * @see [GetSet Command](http://redis.io/commands/getset)
     */
    fun getAndSet(key: String, value: String): String? {
        return valueOps().getAndSet(key, value)
    }

    /**
     * 对 key 进行自增，自增步长为 1
     * @param key 需要自增的 key
     * @return 自增后的 value 值
     * @see this.incrementBy
     */
    fun increment(key: String): Long {
        val l = valueOps().increment(key)
        return l ?: 0
    }

    /**
     * 对 key 进行自增，并指定自增步长, 当 key 不存在时先创建一个值为 0 的 key，再进行自增
     * @param key 需要自增的 key
     * @param delta 自增的步长
     * @return 自增后的 value 值
     * @see [IncrBy Command](http://redis.io/commands/incrby)
     */
    fun incrementBy(key: String, delta: Long): Long {
        val l = valueOps().increment(key, delta)
        return l ?: 0
    }

    /**
     * @see this.incrementBy
     */
    fun incrementByFloat(key: String, delta: Double): Double {
        val d = valueOps().increment(key, delta)
        return d ?: 0.0
    }

    /**
     * 从指定的 keys 批量获取 values
     * @param keys keys
     * @return values list，当值为空时，该 key 对应的 value 为 null
     * @see [MGet Command](http://redis.io/commands/mget)
     */
    fun multiGet(keys: Collection<String>): MutableList<String?> {
        return valueOps().multiGet(keys)
    }

    /**
     * @see this.multiGet
     */
    fun multiGet(vararg keys: String): MutableList<String?> {
        return multiGet(keys.toList())
    }

    /**
     * 批量获取 keys 的值，并返回一个 map
     * @param keys keys
     * @return map，key 和 value 的键值对集合，当 value 获取为 null 时，不存入此 map
     */
    fun multiGetMap(keys: Collection<String>): Map<String, String> {
        return multiGetMap<String>(keys, Function { t: String -> t })
    }

    /**
     * 获取多个key 并且值组装为map
     * @param keys key
     * @param convert 值序列化方法
     * @return java.util.Map<java.lang.String></java.lang.String>, T> map，key 和 value 的键值对集合，当 value 获取为 null
     * 时，不存入此 map
     */
    fun <T> multiGetMap(keys: Collection<String>, convert: Function<String, T>): Map<String, T> {
        val map: MutableMap<String, T> = HashMap<String, T>()
        if (keys.isEmpty()) {
            return map
        }
        val values = valueOps().multiGet(keys)
        if (values.isNullOrEmpty()) {
            return map
        }
        val keysIterator: Iterator<String> = keys.iterator()
        val valuesIterator: Iterator<String?> = values.iterator()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            val value = valuesIterator.next()
            if (value != null) {
                val t = convert.apply(value)
                map.put(key, t)
            }
        }

        return map
    }

    /**
     * @see this.multiGetMap
     */
    fun multiGetMap(vararg keys: String): Map<String, String> {
        return multiGetMap(keys.toList())
    }

    /**
     * 设置 value for key
     * @param key 指定的 key
     * @param value 值
     * @see [Set Command](https://redis.io/commands/set)
     */
    fun set(key: String, value: String) {
        valueOps().set(key, value)
    }

    /**
     * 设置 value for key, 同时为其设置过期时间
     * @param key key
     * @param value value
     * @param timeout 过期时间 单位：秒
     * @see this.set
     */
    @JvmOverloads
    fun set(key: String, value: String, timeout: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        valueOps().set(key, value, timeout, timeUnit)
    }

    /**
     * 设置 value for key, 同时为其设置其在指定时间过期
     * @param key key
     * @param value value
     * @param expireTime 在指定时间过期
     */
    fun set(key: String, value: String, expireTime: Instant) {
        val timeout = expireTime.epochSecond - DateTime.instant().epochSecond
        set(key, value, timeout)
    }

    fun set(key: String, value: String, duration: Duration) {
        set(key, value, duration.toSeconds())
    }

    /**
     * 当 key 不存在时，进行 value 设置，当 key 存在时不执行操作
     * @param key key
     * @param value value
     * @return boolean
     * @see [SetNX Command](https://redis.io/commands/setnx)
     */
    fun setIfAbsent(key: String, value: String): Boolean {
        return true == valueOps().setIfAbsent(key, value)
    }

    fun setIfAbsent(key: String, value: String, duration: Duration): Boolean {
        return true == valueOps().setIfAbsent(key, value, duration)
    }

    /**
     * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
     * @param key key
     * @param value value
     * @param timeout 过期时间
     * @return boolean 操作是否成功
     * @see [SetNX Command](https://redis.io/commands/setnx)
     */
    fun setIfAbsent(key: String, value: String, timeout: Long): Boolean {
        return setIfAbsent(key, value, timeout, TimeUnit.SECONDS)
    }

    /**
     * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
     * @param key key
     * @param value value
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return boolean 操作是否成功
     * @see [SetNX Command](https://redis.io/commands/setnx)
     */
    fun setIfAbsent(key: String, value: String, timeout: Long, timeUnit: TimeUnit): Boolean {
        return true == valueOps().setIfAbsent(key, value, timeout, timeUnit)
    }
// endregion

}
