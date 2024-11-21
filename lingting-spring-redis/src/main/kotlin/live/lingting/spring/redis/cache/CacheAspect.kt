package live.lingting.spring.redis.cache

import java.time.Duration
import live.lingting.framework.function.ThrowableSupplier
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.properties.RedisProperties
import live.lingting.spring.util.AspectUtils.getMethod
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.util.Assert
import org.springframework.util.CollectionUtils

/**
 * @author lingting 2024-04-17 20:24
 */
@Order
@Aspect
class CacheAspect(private val redis: Redis, private val properties: RedisProperties) : Ordered {
    @Pointcut("@annotation(live.lingting.spring.redis.cache.Cached) || @annotation(live.lingting.spring.redis.cache.CacheClear) || @annotation(live.lingting.spring.redis.cache.CacheBatchClear)")
    fun pointCut() {
        // do nothing
    }

    @Around("pointCut()")

    fun around(point: ProceedingJoinPoint): Any {
        val target = point.getTarget()
        val method = getMethod(point)
        val args = point.getArgs()

        if (method == null) {
            return point.proceed()
        }

        val type = method.getReturnType() as Class<Any>
        val generator = CacheKeyGenerator(properties.getKeyDelimiter(), target, method, args)
        val cached = method.getAnnotation<Cached>(Cached::class.java)
        val cacheClear = method.getAnnotation<CacheClear>(CacheClear::class.java)
        val cacheBatchClear = method.getAnnotation<CacheBatchClear>(CacheBatchClear::class.java)

        var result: Any = null
        if (cached != null) {
            val key = generator.cached(cached)
            Assert.hasText(key, "Cached key is required!")
            val ttl = cached.ttl
            val expireTime: Duration
            if (ttl > 0) {
                expireTime = Duration.ofSeconds(ttl)
            } else if (ttl < 0) {
                expireTime = null
            } else {
                expireTime = properties.getCacheExpireTime()
            }

            val cache = redis.cache(expireTime, properties.getLockTimeout(), properties.getLeaseTime())

            val onLockFailure = ThrowableSupplier { cache.get<Any>(key, type) }
            result = if (cached.ifAbsent)
                cache.setIfAbsent<Any>(key, ThrowableSupplier { point.proceed() }, onLockFailure, type)
            else
                cache.set<Any>(key, ThrowableSupplier { point.proceed() }, onLockFailure)
        }

        if (cacheClear != null || cacheBatchClear != null) {
            val keys = generator.cacheClear(cacheClear, cacheBatchClear)
            // 如果未执行
            if (cached == null) {
                result = point.proceed()
            }
            // 如果已执行, 直接删除缓存key
            if (!CollectionUtils.isEmpty(keys)) {
                redis.template().delete(keys)
            }
        }

        return result
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}
