package live.lingting.spring.redis.cache;

import live.lingting.framework.function.ThrowingSupplier;
import live.lingting.spring.redis.Redis;
import live.lingting.spring.redis.properties.RedisProperties;
import live.lingting.spring.util.AspectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

/**
 * @author lingting 2024-04-17 20:24
 */
@Order
@Aspect
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CacheAspect implements Ordered {

	private final Redis redis;

	private final RedisProperties properties;

	@Pointcut("@annotation(live.lingting.spring.redis.cache.Cached) || @annotation(live.lingting.spring.redis.cache.CacheClear) || @annotation(live.lingting.spring.redis.cache.CacheBatchClear)")
	public void pointCut() {
		// do nothing
	}

	@SneakyThrows
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object target = point.getTarget();
		Method method = AspectUtils.getMethod(point);
		Object[] args = point.getArgs();

		if (method == null) {
			return point.proceed();
		}

		Class<Object> type = (Class<Object>) method.getReturnType();
		CacheKeyGenerator generator = new CacheKeyGenerator(properties.getKeyDelimiter(), target, method, args);
		Cached cached = method.getAnnotation(Cached.class);
		CacheClear cacheClear = method.getAnnotation(CacheClear.class);
		CacheBatchClear cacheBatchClear = method.getAnnotation(CacheBatchClear.class);

		Object result = null;
		if (cached != null) {
			String key = generator.cached(cached);
			Assert.hasText(key, "Cached key is required!");
			long ttl = cached.ttl();
			Duration expireTime;
			if (ttl > 0) {
				expireTime = Duration.ofSeconds(ttl);
			}
			else if (ttl < 0) {
				expireTime = null;
			}
			else {
				expireTime = properties.getCacheExpireTime();
			}

			RedisCache cache = new RedisCache(redis, properties.getNullValue(), expireTime, properties.getLockTimeout(),
					properties.getLeaseTime());

			ThrowingSupplier<Object> onLockFailure = () -> cache.get(key, type);
			result = cached.ifAbsent() ? cache.setIfAbsent(key, point::proceed, onLockFailure, type)
					: cache.set(key, point::proceed, onLockFailure);
		}

		if (cacheClear != null || cacheBatchClear != null) {
			List<String> keys = generator.cacheClear(cacheClear, cacheBatchClear);
			// 如果未执行
			if (cached == null) {
				result = point.proceed();
			}
			// 如果已执行, 直接删除缓存key
			if (!CollectionUtils.isEmpty(keys)) {
				redis.template().delete(keys);
			}
		}

		return result;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
