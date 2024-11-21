package live.lingting.spring.redis.configuration

import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.cache.CacheAspect
import live.lingting.spring.redis.prefix.DefaultKeyPrefixConvert
import live.lingting.spring.redis.prefix.JdkKeyPrefixSerializer
import live.lingting.spring.redis.prefix.KeyPrefixConvert
import live.lingting.spring.redis.prefix.StringKeyPrefixSerializer
import live.lingting.spring.redis.properties.RedisProperties
import live.lingting.spring.redis.script.RedisScriptProvider
import org.redisson.Redisson
import org.redisson.spring.starter.RedissonAutoConfiguration
import org.redisson.spring.starter.RedissonAutoConfigurationV2
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.StreamOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.core.ZSetOperations

/**
 * @author lingting 2024-04-17 14:41
 */
@EnableConfigurationProperties(RedisProperties::class)
@AutoConfiguration(before = [RedissonAutoConfiguration::class, RedissonAutoConfigurationV2::class])
class SpringRedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun keyPrefixConvert(properties: RedisProperties): KeyPrefixConvert {
        return DefaultKeyPrefixConvert(properties)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(KeyPrefixConvert::class)
    fun jdkKeyPrefixSerializer(convert: KeyPrefixConvert): JdkKeyPrefixSerializer {
        return JdkKeyPrefixSerializer(convert)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(KeyPrefixConvert::class)
    fun stringKeyPrefixSerializer(convert: KeyPrefixConvert): StringKeyPrefixSerializer {
        return StringKeyPrefixSerializer(convert)
    }

    @Bean
    @ConditionalOnBean(JdkKeyPrefixSerializer::class)
    @ConditionalOnMissingBean(name = ["redisTemplate"])
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        serializer: JdkKeyPrefixSerializer
    ): RedisTemplate<Any, Any> {
        val template = RedisTemplate<Any, Any>()
        template.setConnectionFactory(redisConnectionFactory)
        template.setKeySerializer(serializer)
        return template
    }

    @Bean
    @ConditionalOnBean(StringKeyPrefixSerializer::class)
    @ConditionalOnMissingBean(StringRedisTemplate::class)
    fun stringRedisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        serializer: StringKeyPrefixSerializer
    ): StringRedisTemplate {
        val template = StringRedisTemplate()
        template.setConnectionFactory(redisConnectionFactory)
        template.setKeySerializer(serializer)
        return template
    }

    @Bean
    @ConditionalOnMissingBean
    fun hashOps(template: StringRedisTemplate): HashOperations<String, String, String> {
        return template.opsForHash<String, String>()
    }

    @Bean
    @ConditionalOnMissingBean
    fun valueOps(template: StringRedisTemplate): ValueOperations<String, String> {
        return template.opsForValue()
    }

    @Bean
    @ConditionalOnMissingBean
    fun listOps(template: StringRedisTemplate): ListOperations<String, String> {
        return template.opsForList()
    }

    @Bean
    @ConditionalOnMissingBean
    fun setOps(template: StringRedisTemplate): SetOperations<String, String> {
        return template.opsForSet()
    }

    @Bean
    @ConditionalOnMissingBean
    fun zSetOps(template: StringRedisTemplate): ZSetOperations<String, String> {
        return template.opsForZSet()
    }

    @Bean
    @ConditionalOnMissingBean
    fun streamOps(template: StringRedisTemplate): StreamOperations<String, String, String> {
        return template.opsForStream<String, String>()
    }

    @Bean
    @ConditionalOnMissingBean
    fun redis(
        template: StringRedisTemplate, redisson: Redisson, properties: RedisProperties,
        providers: MutableList<RedisScriptProvider>
    ): Redis {
        return Redis(template, redisson, properties, providers)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(Redis::class)
    fun cacheAspect(redis: Redis, properties: RedisProperties): CacheAspect {
        return CacheAspect(redis, properties)
    }
}
