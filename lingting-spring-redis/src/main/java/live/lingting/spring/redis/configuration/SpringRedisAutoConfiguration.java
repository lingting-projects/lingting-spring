package live.lingting.spring.redis.configuration;

import live.lingting.spring.redis.Redis;
import live.lingting.spring.redis.cache.CacheAspect;
import live.lingting.spring.redis.prefix.DefaultKeyPrefixConvert;
import live.lingting.spring.redis.prefix.JdkKeyPrefixSerializer;
import live.lingting.spring.redis.prefix.KeyPrefixConvert;
import live.lingting.spring.redis.prefix.StringKeyPrefixSerializer;
import live.lingting.spring.redis.properties.RedisProperties;
import live.lingting.spring.redis.script.RedisScriptProvider;
import org.redisson.Redisson;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author lingting 2024-04-17 14:41
 */
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfiguration(before = { RedissonAutoConfiguration.class, RedissonAutoConfigurationV2.class })
public class SpringRedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public KeyPrefixConvert keyPrefixConvert(RedisProperties properties) {
		return new DefaultKeyPrefixConvert(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(KeyPrefixConvert.class)
	public JdkKeyPrefixSerializer jdkKeyPrefixSerializer(KeyPrefixConvert convert) {
		return new JdkKeyPrefixSerializer(convert);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(KeyPrefixConvert.class)
	public StringKeyPrefixSerializer stringKeyPrefixSerializer(KeyPrefixConvert convert) {
		return new StringKeyPrefixSerializer(convert);
	}

	@Bean
	@ConditionalOnBean(JdkKeyPrefixSerializer.class)
	@ConditionalOnMissingBean(name = { "redisTemplate" })
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
			JdkKeyPrefixSerializer serializer) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(serializer);
		return template;
	}

	@Bean
	@ConditionalOnBean(StringKeyPrefixSerializer.class)
	@ConditionalOnMissingBean({ StringRedisTemplate.class })
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory,
			StringKeyPrefixSerializer serializer) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(serializer);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public Redis redis(StringRedisTemplate template, Redisson redisson, RedisProperties properties,
			List<RedisScriptProvider> providers) {
		return new Redis(template, redisson, properties, providers);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(Redis.class)
	public CacheAspect cacheAspect(Redis redis, RedisProperties properties) {
		return new CacheAspect(redis, properties);
	}

}
