package live.lingting.spring.redis.properties

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-04-17 15:04
 */
@ConfigurationProperties(RedisProperties.PREFIX)
class RedisProperties {
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
    var leaseTime: Duration = Duration.ofSeconds(30)

    override fun equals(o: Any): Boolean {
        if (o === this) return true
        if (o !is RedisProperties) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$keyPrefix`: Any = this.keyPrefix
        val `other$keyPrefix`: Any = other.keyPrefix
        if (if (`this$keyPrefix` == null) `other$keyPrefix` != null else (`this$keyPrefix` != `other$keyPrefix`)) return false
        val `this$keyDelimiter`: Any = this.keyDelimiter
        val `other$keyDelimiter`: Any = other.keyDelimiter
        if (if (`this$keyDelimiter` == null) `other$keyDelimiter` != null else (`this$keyDelimiter` != `other$keyDelimiter`)) return false
        val `this$nullValue`: Any = this.nullValue
        val `other$nullValue`: Any = other.nullValue
        if (if (`this$nullValue` == null) `other$nullValue` != null else (`this$nullValue` != `other$nullValue`)) return false
        val `this$cacheExpireTime`: Any = this.cacheExpireTime
        val `other$cacheExpireTime`: Any = other.cacheExpireTime
        if (if (`this$cacheExpireTime` == null)
                `other$cacheExpireTime` != null
            else (`this$cacheExpireTime` != `other$cacheExpireTime`)
        ) return false
        val `this$lockTimeout`: Any = this.lockTimeout
        val `other$lockTimeout`: Any = other.lockTimeout
        if (if (`this$lockTimeout` == null) `other$lockTimeout` != null else (`this$lockTimeout` != `other$lockTimeout`)) return false
        val `this$leaseTime`: Any = this.leaseTime
        val `other$leaseTime`: Any = other.leaseTime
        if (if (`this$leaseTime` == null) `other$leaseTime` != null else (`this$leaseTime` != `other$leaseTime`)) return false
        return true
    }

    protected fun canEqual(other: Any): Boolean {
        return other is RedisProperties
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$keyPrefix`: Any = this.keyPrefix
        result = result * PRIME + (if (`$keyPrefix` == null) 43 else `$keyPrefix`.hashCode())
        val `$keyDelimiter`: Any = this.keyDelimiter
        result = result * PRIME + (if (`$keyDelimiter` == null) 43 else `$keyDelimiter`.hashCode())
        val `$nullValue`: Any = this.nullValue
        result = result * PRIME + (if (`$nullValue` == null) 43 else `$nullValue`.hashCode())
        val `$cacheExpireTime`: Any = this.cacheExpireTime
        result = result * PRIME + (if (`$cacheExpireTime` == null) 43 else `$cacheExpireTime`.hashCode())
        val `$lockTimeout`: Any = this.lockTimeout
        result = result * PRIME + (if (`$lockTimeout` == null) 43 else `$lockTimeout`.hashCode())
        val `$leaseTime`: Any = this.leaseTime
        result = result * PRIME + (if (`$leaseTime` == null) 43 else `$leaseTime`.hashCode())
        return result
    }

    override fun toString(): String {
        return ("RedisProperties(keyPrefix=" + this.keyPrefix + ", keyDelimiter=" + this.keyDelimiter
                + ", nullValue=" + this.nullValue + ", cacheExpireTime=" + this.cacheExpireTime
                + ", lockTimeout=" + this.lockTimeout + ", leaseTime=" + this.leaseTime + ")")
    }

    companion object {
        const val PREFIX: String = "lingting.redis"
    }
}
