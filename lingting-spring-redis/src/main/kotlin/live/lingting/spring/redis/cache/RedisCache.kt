package live.lingting.spring.redis.cache

import live.lingting.framework.function.ThrowableSupplier
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.framework.jackson.JacksonUtils.toObj
import live.lingting.framework.lock.SpinLock
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.lock.RedisLockParams
import live.lingting.spring.redis.properties.RedisProperties
import java.time.Duration
import java.util.function.Function

/**
 * @author lingting 2024-04-17 19:11
 */
@Suppress("UNCHECKED_CAST")
open class RedisCache(
    val key: String,
    val redis: Redis,
    val nullValue: String,
    val expireTime: Duration?,
    val lockTimeout: Duration,
    val leaseTime: Duration?
) {

    constructor(key: String, redis: Redis, expireTime: Duration?, properties: RedisProperties) : this(
        key,
        redis,
        properties.nullValue,
        expireTime,
        properties.lockTimeout,
        properties.leaseTime
    )

    constructor(key: String, redis: Redis, properties: RedisProperties) : this(
        key,
        redis,
        properties.nullValue,
        properties.cacheExpireTime,
        properties.lockTimeout,
        properties.leaseTime
    )

    protected fun lock(): SpinLock {
        return redis.lock("$key:locker", RedisLockParams(expire = leaseTime)).spin()
    }

    protected fun get(): String? {
        return get<String>(Function { s -> s })
    }

    fun <T> get(tClass: Class<T>): T? {
        return get<T>(Function { v ->
            if (String::class.java.isAssignableFrom(tClass)) {
                return@Function v as T
            }
            toObj<T>(v, tClass)
        })
    }

    fun <T> get(deserialize: Function<String, T>): T? {
        val ops = redis.valueOps()
        val cache = if (expireTime != null) ops.getAndExpire(key, expireTime) else ops.get(key)
        if (nullValue == cache) {
            return null
        }
        // 没有key, 返回null
        if (cache == null) {
            return null
        }
        return deserialize.apply(cache)
    }

    fun <T> set(t: T) {
        set<T>(t, Function { obj -> JacksonUtils.toJson(obj) })
    }

    fun <T> set(t: T, serialize: Function<T, String>) {
        val value: String = (if (t == null) nullValue else serialize.apply(t))
        val ops = redis.valueOps()
        if (expireTime != null) {
            ops.set(key, value, expireTime)
        } else {
            ops.set(key, value)
        }
    }

    fun <T> set(onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T>): T {
        return set<T>(onGet, onLockFailure, Function { obj -> JacksonUtils.toJson(obj) })
    }

    fun <T> set(
        onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T>,
        serialize: Function<T, String>
    ): T {
        val lock = lock()
        if (lock.tryLock(lockTimeout)) {
            try {
                val t = onGet.get()
                set<T>(t, serialize)
                return t
            } finally {
                lock.unlock()
            }
        }

        return onLockFailure.get()
    }

    fun <T> setIfAbsent(
        onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T?>,
        tClass: Class<T>
    ): T? {
        return setIfAbsent<T>(onGet, onLockFailure, Function { obj -> JacksonUtils.toJson(obj) }, Function { v -> toObj<T>(v, tClass) })
    }

    fun <T> setIfAbsent(
        onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T?>,
        serialize: Function<T, String>, deserialize: Function<String, T>
    ): T? {
        val spinLock = lock()

        if (spinLock.tryLock(lockTimeout)) {
            try {
                val cache = get()
                if (nullValue == cache) {
                    return null
                }

                if (cache == null) {
                    val t = onGet.get()
                    set<T>(t, serialize)
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
