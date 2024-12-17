package live.lingting.spring.redis.script

import java.util.concurrent.ConcurrentHashMap
import live.lingting.spring.redis.Redis
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.core.script.DigestUtils
import org.springframework.data.redis.core.script.RedisScript

/**
 * @author lingting 2024-04-17 16:19
 */
class RepeatRedisScript<T> @JvmOverloads constructor(
    source: String,
    val type: ReturnType = ReturnType.STATUS
) {

    companion object {

        @JvmField
        val CACHE = ConcurrentHashMap<RedisScript<*>, RepeatRedisScript<*>>()

        @JvmStatic
        fun <T> of(source: RedisScript<T>): RepeatRedisScript<T> {
            return CACHE.computeIfAbsent(source) { RepeatRedisScript(source.scriptAsString, source.resultType) } as RepeatRedisScript<T>
        }

        fun <T> RedisScript<T>.repeat(): RepeatRedisScript<T> = of(this)

    }

    val source: String

    val sha1: String

    var load: Boolean = false

    var bytes: ByteArray? = null

    constructor(source: String, resultType: Class<T>?) : this(source, ReturnType.fromJavaType(resultType))

    init {
        require(source.isNotBlank()) { "Redis script source must not be empty" }
        this.source = source
        this.sha1 = DigestUtils.sha1DigestAsHex(source)
    }

    @JvmOverloads
    fun execute(keys: List<String> = emptyList(), vararg args: Any): T? {
        return execute(keys, args.toList())
    }

    @JvmOverloads
    fun execute(keys: List<String> = emptyList(), args: Collection<Any>): T? {
        return Redis.instance().script(this).execute(keys, args)
    }


}
