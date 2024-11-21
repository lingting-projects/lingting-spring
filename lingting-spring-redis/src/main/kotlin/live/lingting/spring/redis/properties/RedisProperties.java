package live.lingting.spring.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lingting 2024-04-17 15:04
 */
@ConfigurationProperties(RedisProperties.PREFIX)
public class RedisProperties {

	public static final String PREFIX = "lingting.redis";

	private String keyPrefix = "lingting:";

	private String keyDelimiter = ":";

	private String nullValue = "N_V";

	/**
	 * 缓存过期时间
	 */
	private Duration cacheExpireTime = Duration.ofDays(7);

	/**
	 * 锁获取超时时间
	 */
	private Duration lockTimeout = Duration.ofSeconds(10);

	/**
	 * 锁持有最长时间, 超过该时间还未完成缓存操作则直接释放锁
	 */
	private Duration leaseTime = Duration.ofSeconds(30);

	public RedisProperties() {
	}

	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public String getKeyDelimiter() {
		return this.keyDelimiter;
	}

	public String getNullValue() {
		return this.nullValue;
	}

	public Duration getCacheExpireTime() {
		return this.cacheExpireTime;
	}

	public Duration getLockTimeout() {
		return this.lockTimeout;
	}

	public Duration getLeaseTime() {
		return this.leaseTime;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public void setKeyDelimiter(String keyDelimiter) {
		this.keyDelimiter = keyDelimiter;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public void setCacheExpireTime(Duration cacheExpireTime) {
		this.cacheExpireTime = cacheExpireTime;
	}

	public void setLockTimeout(Duration lockTimeout) {
		this.lockTimeout = lockTimeout;
	}

	public void setLeaseTime(Duration leaseTime) {
		this.leaseTime = leaseTime;
	}

	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RedisProperties))
			return false;
		final RedisProperties other = (RedisProperties) o;
		if (!other.canEqual((Object) this))
			return false;
		final Object this$keyPrefix = this.getKeyPrefix();
		final Object other$keyPrefix = other.getKeyPrefix();
		if (this$keyPrefix == null ? other$keyPrefix != null : !this$keyPrefix.equals(other$keyPrefix))
			return false;
		final Object this$keyDelimiter = this.getKeyDelimiter();
		final Object other$keyDelimiter = other.getKeyDelimiter();
		if (this$keyDelimiter == null ? other$keyDelimiter != null : !this$keyDelimiter.equals(other$keyDelimiter))
			return false;
		final Object this$nullValue = this.getNullValue();
		final Object other$nullValue = other.getNullValue();
		if (this$nullValue == null ? other$nullValue != null : !this$nullValue.equals(other$nullValue))
			return false;
		final Object this$cacheExpireTime = this.getCacheExpireTime();
		final Object other$cacheExpireTime = other.getCacheExpireTime();
		if (this$cacheExpireTime == null ? other$cacheExpireTime != null
				: !this$cacheExpireTime.equals(other$cacheExpireTime))
			return false;
		final Object this$lockTimeout = this.getLockTimeout();
		final Object other$lockTimeout = other.getLockTimeout();
		if (this$lockTimeout == null ? other$lockTimeout != null : !this$lockTimeout.equals(other$lockTimeout))
			return false;
		final Object this$leaseTime = this.getLeaseTime();
		final Object other$leaseTime = other.getLeaseTime();
		if (this$leaseTime == null ? other$leaseTime != null : !this$leaseTime.equals(other$leaseTime))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof RedisProperties;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $keyPrefix = this.getKeyPrefix();
		result = result * PRIME + ($keyPrefix == null ? 43 : $keyPrefix.hashCode());
		final Object $keyDelimiter = this.getKeyDelimiter();
		result = result * PRIME + ($keyDelimiter == null ? 43 : $keyDelimiter.hashCode());
		final Object $nullValue = this.getNullValue();
		result = result * PRIME + ($nullValue == null ? 43 : $nullValue.hashCode());
		final Object $cacheExpireTime = this.getCacheExpireTime();
		result = result * PRIME + ($cacheExpireTime == null ? 43 : $cacheExpireTime.hashCode());
		final Object $lockTimeout = this.getLockTimeout();
		result = result * PRIME + ($lockTimeout == null ? 43 : $lockTimeout.hashCode());
		final Object $leaseTime = this.getLeaseTime();
		result = result * PRIME + ($leaseTime == null ? 43 : $leaseTime.hashCode());
		return result;
	}

	public String toString() {
		return "RedisProperties(keyPrefix=" + this.getKeyPrefix() + ", keyDelimiter=" + this.getKeyDelimiter()
				+ ", nullValue=" + this.getNullValue() + ", cacheExpireTime=" + this.getCacheExpireTime()
				+ ", lockTimeout=" + this.getLockTimeout() + ", leaseTime=" + this.getLeaseTime() + ")";
	}

}
