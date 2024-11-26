package live.lingting.spring.redis.lock

import java.time.Duration
import live.lingting.framework.kt.optional
import live.lingting.framework.lock.AbstractLock
import live.lingting.framework.lock.SpinLock
import live.lingting.framework.util.ValueUtils
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.script.RepeatRedisScript

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

        const val SCRIPT_LOCK_LUA = """
-- 获取传入的参数
local lockKey = KEYS[1]
local counterKey = KEYS[2]
local value = ARGV[1]
local ttl = tonumber(ARGV[2])

-- 获取lockKey当前的值
local currentLockValue = redis.call('GET', lockKey)

-- lockKey 不与传入值相等
if currentLockValue != value then
    return 0
-- lockKey 不存在
elseif currentLockValue == nil or currentLockValue == false then
    redis.call('SET', lockKey, value)
    redis.call('SET', counterKey, 1)
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

        const val SCRIPT_UNLOCK_LUA = """
            -- 获取传入的参数
local lockKey = KEYS[1]
local counterKey = KEYS[2]
local value = ARGV[1]
local ttl = tonumber(ARGV[2])

-- 获取lockKey当前的值
local currentLockValue = redis.call('GET', lockKey)

-- lockKey 不存在 或 不与传入值相等
if currentLockValue == nil or currentLockValue == false or currentLockValue != value then
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

    }

    val id = ValueUtils.simpleUuid()

    val lockKey = "$key:lock"

    val counterKey = "$key:counter"

    override var sleep: Duration
        get() = params.sleep
        set(value) {}

    /**
     * 当前线程获取到锁之后, 给锁设置的值
     */
    val value: String
        get() = "$id:${Thread.currentThread().threadId()}"

    /**
     * 锁的自动释放时间
     */
    val ttl: Long = params.ttl()

    override fun tryLock(): Boolean {
        val keys = listOf(lockKey, counterKey)
        val bool = redis.scriptExecutor().execute(SCRIPT_LOCK, keys, value, ttl)
        return bool == true
    }

    override fun unlock() {
        val keys = listOf(lockKey, counterKey)
        redis.scriptExecutor().execute(SCRIPT_UNLOCK, keys, value, ttl)
    }

    fun count(): Long {
        val get = redis.get(counterKey)
        return get.optional().map { it.toLong() }.orElse(0)
    }

    fun spin(): SpinLock {
        return SpinLock(this, params.sleep)
    }
}