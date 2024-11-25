package live.lingting.spring.redis.properties

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-04-17 15:04
 */
@ConfigurationProperties(RedisProperties.PREFIX)
class RedisProperties {

    companion object {
        const val PREFIX: String = "lingting.redis"
    }

    var keyPrefix: String = "lingting:"

    var keyDelimiter: String = ":"

    var nullValue: String = "N_V"

    /**
     * 缓存过期时间
     */
    var cacheExpireTime: Duration = Duration.ofDays(7)

    /**
     * 锁获取超时时间
     */
    var lockTimeout: Duration = Duration.ofSeconds(10)

    /**
     * 锁持有最长时间, 超过该时间还未完成缓存操作则直接释放锁
     */
    var leaseTime: Duration? = Duration.ofSeconds(30)

}
