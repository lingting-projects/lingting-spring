package live.lingting.spring.redis.cache

import live.lingting.framework.function.ThrowableSupplier
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.properties.RedisProperties
import live.lingting.spring.util.AspectUtils.findMethod
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import java.time.Duration

/**
 * @author lingting 2024-04-17 20:24
 */
@Order
@Aspect
@Suppress("UNCHECKED_CAST")
class CacheAspect(private val redis: Redis, private val properties: RedisProperties) : Ordered {

    @Pointcut("@annotation(live.lingting.spring.redis.cache.Cached) || @annotation(live.lingting.spring.redis.cache.CachedBatch) || @annotation(live.lingting.spring.redis.cache.CacheClear) || @annotation(live.lingting.spring.redis.cache.CacheClearBatch)")
    fun pointCut() {
        // do nothing
    }

    @Around("pointCut()")
    fun around(point: ProceedingJoinPoint): Any? {
        val target = point.target
        val method = findMethod(point)
        val args = point.args

        if (method == null) {
            return point.proceed()
        }

        val type = method.returnType as Class<Any>
        val generator = CacheKeyGenerator(properties.keyDelimiter, target, method, args)
        val cached = method.getAnnotation(Cached::class.java)
        val cachedBatch = method.getAnnotation(CachedBatch::class.java)
        val clear = method.getAnnotation(CacheClear::class.java)
        val clearBatch = method.getAnnotation(CacheClearBatch::class.java)

        var result: Any? = null
        if (cached != null || cachedBatch != null) {
            val keys = generator.cached(cached, cachedBatch)
            val ttl = cached.ttl
            val expireTime = if (ttl > 0) {
                Duration.of(ttl, cached.ttlUnit)
            } else if (ttl < 0) {
                null
            } else {
                properties.cacheExpireTime
            }

            val cache = RedisCache(keys, redis, expireTime, properties)

            val onLockFailure = ThrowableSupplier { cache.get(type) }
            result = if (cached.ifAbsent) {
                cache.setIfAbsent({ point.proceed() }, onLockFailure, type)
            } else {
                cache.set({ point.proceed() }, onLockFailure)
            }
        }

        if (clear != null || clearBatch != null) {
            val keys = generator.cacheClear(clear, clearBatch)
            // 如果未执行
            if (cached == null) {
                result = point.proceed()
            }
            // 删除缓存key
            redis.template().delete(keys)
        }

        return result
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}
