package live.lingting.spring.redis.cache;

import live.lingting.framework.function.ThrowableSupplier;
import live.lingting.framework.jackson.JacksonUtils;
import live.lingting.spring.redis.Redis;
import org.redisson.RedissonSpinLock;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author lingting 2024-04-17 19:11
 */
@SuppressWarnings("unchecked")
public class RedisCache {

	private final Redis redis;

	private final String nullValue;

	private final Duration expireTime;

	private final Duration lockTimeout;

	private final Duration leaseTime;

	public RedisCache(Redis redis, String nullValue, Duration expireTime, Duration lockTimeout, Duration leaseTime) {
		this.redis = redis;
		this.nullValue = nullValue;
		this.expireTime = expireTime;
		this.lockTimeout = lockTimeout;
		this.leaseTime = leaseTime;
	}

	protected String get(String key) {
		return get(key, s -> s);
	}

	public <T> T get(String key, Class<T> tClass) {
		return get(key, v -> {
			if (String.class.isAssignableFrom(tClass)) {
				return (T) v;
			}
			return JacksonUtils.toObj(v, tClass);
		});
	}

	public <T> T get(String key, Function<String, T> deserialize) {
		ValueOperations<String, String> ops = redis.valueOps();
		String cache = expireTime != null ? ops.getAndExpire(key, expireTime) : ops.get(key);
		if (Objects.equals(nullValue, cache)) {
			return null;
		}
		return deserialize.apply(cache);
	}

	public <T> void set(String key, T t) {
		set(key, t, JacksonUtils::toJson);
	}

	public <T> void set(String key, T t, Function<T, String> serialize) {
		String value = t == null ? nullValue : serialize.apply(t);
		ValueOperations<String, String> ops = redis.valueOps();
		if (expireTime != null) {
			ops.set(key, value, expireTime);
		}
		else {
			ops.set(key, value);
		}
	}

	public <T> T set(String key, ThrowableSupplier<T> onGet, ThrowableSupplier<T> onLockFailure) {
		return set(key, onGet, onLockFailure, JacksonUtils::toJson);
	}

	public <T> T set(String key, ThrowableSupplier<T> onGet, ThrowableSupplier<T> onLockFailure,
			Function<T, String> serialize) {
		RedissonSpinLock spinLock = redis.spinLock("%s:%s".formatted(key, "lock"));

		if (spinLock.tryLock(lockTimeout.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)) {
			try {
				T t = onGet.get();
				set(key, t, serialize);
				return t;
			}
			finally {
				spinLock.unlock();
			}
		}

		return onLockFailure.get();
	}

	public <T> T setIfAbsent(String key, ThrowableSupplier<T> onGet, ThrowableSupplier<T> onLockFailure,
			Class<T> tClass) {
		return setIfAbsent(key, onGet, onLockFailure, JacksonUtils::toJson, v -> JacksonUtils.toObj(v, tClass));
	}

	public <T> T setIfAbsent(String key, ThrowableSupplier<T> onGet, ThrowableSupplier<T> onLockFailure,
			Function<T, String> serialize, Function<String, T> deserialize) {
		RedissonSpinLock spinLock = redis.spinLock("%s:%s".formatted(key, "lock"));

		if (spinLock.tryLock(lockTimeout.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)) {
			try {
				String cache = get(key);
				if (Objects.equals(nullValue, cache)) {
					return null;
				}

				if (cache == null) {
					T t = onGet.get();
					set(key, t, serialize);
					return t;
				}

				return deserialize.apply(cache);
			}
			finally {
				spinLock.unlock();
			}
		}

		return onLockFailure.get();
	}

}
