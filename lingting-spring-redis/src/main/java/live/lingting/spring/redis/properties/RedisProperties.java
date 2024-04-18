package live.lingting.spring.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lingting 2024-04-17 15:04
 */
@Data
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

}
