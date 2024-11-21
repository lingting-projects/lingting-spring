package live.lingting.spring.redis.cache

import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Function
import live.lingting.framework.function.ThrowableSupplier
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.framework.jackson.JacksonUtils.toObj
import live.lingting.spring.redis.Redis

/**
 * @author lingting 2024-04-17 19:11
 */
class RedisCache(
    private val redis: Redis,
    private val nullValue: String,
    private val expireTime: Duration?,
    private val lockTimeout: Duration,
    private val leaseTime: Duration
) {
    protected fun get(key: String): String? {
        return get<String>(key, Function { s -> s })
    }

    fun <T> get(key: String, tClass: Class<T>): T? {
        return get<T>(key, Function { v ->
            if (String::class.java.isAssignableFrom(tClass)) {
                return@Function v as T
            }
            toObj<T>(v, tClass)
        })
    }

    fun <T> get(key: String, deserialize: Function<String, T>): T? {
        val ops = redis.valueOps()
        val cache = if (expireTime != null) ops.getAndExpire(key, expireTime) else ops.get(key)
        if (nullValue == cache) {
            return null
        }
        return deserialize.apply(cache)
    }

    fun <T> set(key: String, t: T) {
        set<T>(key, t, Function { obj -> JacksonUtils.toJson(obj) })
    }

    fun <T> set(key: String, t: T, serialize: Function<T, String>) {
        val value: String = (if (t == null) nullValue else serialize.apply(t))!!
        val ops = redis.valueOps()
        if (expireTime != null) {
            ops.set(key, value, expireTime)
        } else {
            ops.set(key, value)
        }
    }

    fun <T> set(key: String, onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T>): T {
        return set<T>(key, onGet, onLockFailure, Function { obj -> JacksonUtils.toJson(obj) })
    }

    fun <T> set(
        key: String, onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T>,
        serialize: Function<T, String>
    ): T {
        val spinLock = redis.spinLock("$key:lock")

        if (spinLock.tryLock(lockTimeout.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)) {
            try {
                val t = onGet.get()
                set<T>(key, t, serialize)
                return t
            } finally {
                spinLock.unlock()
            }
        }

        return onLockFailure.get()
    }

    fun <T> setIfAbsent(
        key: String, onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T?>,
        tClass: Class<T>
    ): T? {
        return setIfAbsent<T>(key, onGet, onLockFailure, Function { obj -> JacksonUtils.toJson(obj) }, Function { v -> toObj<T>(v, tClass) })
    }

    fun <T> setIfAbsent(
        key: String, onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T?>,
        serialize: Function<T, String>, deserialize: Function<String, T>
    ): T? {
        val spinLock = redis.spinLock("$key:lock")

        if (spinLock.tryLock(lockTimeout.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)) {
            try {
                val cache = get(key)
                if (nullValue == cache) {
                    return null
                }

                if (cache == null) {
                    val t = onGet.get()
                    set<T>(key, t, serialize)
                    return t
                }

                return deserialize.apply(cache)
            } finally {
                spinLock.unlock()
            }
        }

        return onLockFailure.get()
    }
}
