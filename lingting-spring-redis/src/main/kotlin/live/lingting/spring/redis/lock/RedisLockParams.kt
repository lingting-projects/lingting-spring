package live.lingting.spring.redis.lock

import java.time.Duration
import live.lingting.framework.kt.milliseconds

/**
 * @author lingting 2024/11/25 20:08
 */
data class RedisLockParams @JvmOverloads constructor(
    /**
     * 锁如果一直不释放, 则在多久后自动释放
     */
    val timeout: Duration? = null,
    /**
     * 当重复尝试获取时, 每次获取失败后的休眠时间
     */
    val sleep: Duration = 10.milliseconds,
) {

    companion object {

        @JvmStatic
        val DEFAULT = RedisLockParams()

    }

    /**
     * 转为redis的过期时间. 单位: 秒
     */
    fun ttl(): Long {
        if (timeout == null || timeout.isZero || timeout.isNegative) {
            return -1
        }
        return timeout.seconds
    }

}
