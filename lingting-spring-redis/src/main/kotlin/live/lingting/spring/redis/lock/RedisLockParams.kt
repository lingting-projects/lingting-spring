package live.lingting.spring.redis.lock

import live.lingting.framework.util.DurationUtils.hours
import live.lingting.framework.util.DurationUtils.millis
import java.time.Duration

/**
 * @author lingting 2024/11/25 20:08
 */
data class RedisLockParams @JvmOverloads constructor(
    /**
     * 锁如果一直不释放, 则在多久后自动释放
     */
    val expire: Duration? = 1.hours,
    /**
     * 当重复尝试获取时, 每次获取失败后的休眠时间
     */
    val sleep: Duration = 10.millis,
) {

    companion object {

        @JvmStatic
        val DEFAULT = RedisLockParams()

        @JvmStatic
        @JvmOverloads
        fun copy(expire: Duration? = DEFAULT.expire, sleep: Duration = DEFAULT.sleep): RedisLockParams {
            return RedisLockParams(expire, sleep)
        }

    }

    /**
     * 转为redis的过期时间. 单位: 秒
     */
    fun ttl(): Long {
        if (expire == null || expire.isZero || expire.isNegative) {
            return -1
        }
        return expire.seconds
    }

}
