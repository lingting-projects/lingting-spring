package live.lingting.spring.redis.lock

import live.lingting.framework.lock.AbstractLock
import live.lingting.framework.lock.SpinLock
import live.lingting.framework.util.OptionalUtils.optional
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.script.RepeatRedisScript
import live.lingting.spring.redis.unique.RedisId
import java.time.Duration

/**
 * 基于redis的分布式锁
 * @author lingting 2024/11/25 19:58
 */
class RedisLock @JvmOverloads constructor(
    val key: String,
    val params: RedisLockParams = RedisLockParams.DEFAULT,
    val redis: Redis = Redis.instance(),
) : AbstractLock() {

    companion object {
        private val log = logger()

        const val SCRIPT_LOCK_LUA = """-- 获取传入的参数
local lockKey = KEYS[1]
local counterKey = KEYS[2]
local value = ARGV[1]
local ttl = tonumber(ARGV[2])

-- 获取lockKey当前的值
-- 当key不存在时, 这里的返回值是false 不是nil
local lockValue = redis.call('GET', lockKey)

-- lockKey不存在
if lockValue == false then
    redis.call('SET', lockKey, value)
    redis.call('SET', counterKey, 1)
-- lockKey与传入值不相等
elseif lockValue ~= value then
    return 0
-- lockKey 相等
else
    redis.call('INCR', counterKey)
end

if ttl > 0 then
    redis.call('EXPIRE', lockKey, ttl)
    redis.call('EXPIRE', counterKey, ttl)
end

return 1
        """

        @JvmField
        val SCRIPT_LOCK = RepeatRedisScript(SCRIPT_LOCK_LUA, Boolean::class.java)

        const val SCRIPT_UNLOCK_LUA = """-- 获取传入的参数
local lockKey = KEYS[1]
local counterKey = KEYS[2]
local value = ARGV[1]
local ttl = tonumber(ARGV[2])

-- 获取lockKey当前的值
local lockValue = redis.call('GET', lockKey)

-- lockKey 不存在 或 不与传入值相等
if lockValue == false or lockValue ~= value then
    return 0
end
-- lockKey 相等, 计数-1
local newCount = tonumber(redis.call('DECR', counterKey))

if newCount < 1 then
    redis.call('DEL', lockKey)
    redis.call('DEL', counterKey)
end

if ttl > 0 and newCount > 0 then
    redis.call('EXPIRE', lockKey, ttl)
    redis.call('EXPIRE', counterKey, ttl)
end

return 1
        """

        @JvmField
        val SCRIPT_UNLOCK = RepeatRedisScript(SCRIPT_UNLOCK_LUA, Boolean::class.java)

        @JvmStatic
        @JvmOverloads
        fun spin(key: String, params: RedisLockParams = RedisLockParams.DEFAULT, redis: Redis = Redis.instance()): SpinLock {
            val lock = RedisLock(key, params, redis)
            return SpinLock(lock, params.sleep)
        }

        val id by lazy {
            RedisId.get().also {
                log.info("RedisLock id: {}", it)
            }
        }

    }

    val lockExecutor = redis.script(SCRIPT_LOCK)

    val unlockExecutor = redis.script(SCRIPT_UNLOCK)

    val lockKey = "$key:lock"

    val counterKey = "$lockKey:counter"

    override var sleep: Duration
        get() = params.sleep
        set(value) {}

    /**
     * 当前线程获取到锁之后, 给锁设置的值
     * - 同一个线程, 即便不同的实例,也要是同一个值
     */
    val value: String
        get() = "$id:${Thread.currentThread().threadId()}"

    /**
     * 锁的自动释放时间
     */
    val ttl: Long = params.ttl()

    override fun tryLock(): Boolean {
        val keys = listOf(lockKey, counterKey)
        val bool = lockExecutor.execute(keys, value, ttl)
        return bool == true
    }

    override fun unlock() {
        val keys = listOf(lockKey, counterKey)
        unlockExecutor.execute(keys, value, ttl)
    }

    fun count(): Long {
        val get = redis.get(counterKey)
        return get.optional().map { it.toLong() }.orElse(0)
    }

    fun spin(): SpinLock {
        return SpinLock(this, params.sleep)
    }
}
