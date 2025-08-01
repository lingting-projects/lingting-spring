package live.lingting.spring.redis.cache

import live.lingting.framework.function.ThrowableSupplier
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.framework.jackson.JacksonUtils.toObj
import live.lingting.framework.lock.SpinLock
import live.lingting.framework.util.DigestUtils
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
    val keys: List<String>,
    val redis: Redis,
    val nullValue: String,
    val expireTime: Duration?,
    val lockTimeout: Duration,
    val leaseTime: Duration?
) {

    constructor(keys: List<String>, redis: Redis, expireTime: Duration?, properties: RedisProperties) : this(
        keys,
        redis,
        properties.nullValue,
        expireTime,
        properties.lockTimeout,
        properties.leaseTime
    )

    constructor(keys: List<String>, redis: Redis, properties: RedisProperties) : this(
        keys,
        redis,
        properties.nullValue,
        properties.cacheExpireTime,
        properties.lockTimeout,
        properties.leaseTime
    )

    protected fun lock(): SpinLock {
        val join = keys.joinToString("_")
        val hex = DigestUtils.sha1Hex(join)
        return redis.lock("redis:cache:$hex", RedisLockParams(expire = leaseTime)).spin()
    }

    protected fun get(): String? {
        return get { s -> s }
    }

    fun <T> get(tClass: Class<T>): T? {
        return get(Function { v ->
            if (String::class.java.isAssignableFrom(tClass)) {
                return@Function v as T
            }
            toObj(v, tClass)
        })
    }

    fun <T> get(deserialize: Function<String, T>): T? {
        val ops = redis.valueOps()
        // 所有的key都是同一个值, 所有随便拿一个就好了
        val key = keys.first()
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
        set(t) { obj -> JacksonUtils.toJson(obj) }
    }

    fun <T> set(t: T, serialize: Function<T, String>) {
        val value: String = (if (t == null) nullValue else serialize.apply(t))
        redis.multiSet(keys, value, expireTime)
    }

    fun <T> set(onGet: ThrowableSupplier<T>, onLockFailure: ThrowableSupplier<T>): T {
        return set(onGet, onLockFailure) { obj -> JacksonUtils.toJson(obj) }
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
        return setIfAbsent(
            onGet,
            onLockFailure,
            { obj -> JacksonUtils.toJson(obj) },
            { v -> JacksonUtils.toObj(v, tClass) }
        )
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
