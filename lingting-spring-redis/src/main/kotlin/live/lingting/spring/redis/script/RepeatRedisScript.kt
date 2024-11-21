package live.lingting.spring.redis.script

import live.lingting.spring.redis.Redis
import org.redisson.api.RedissonClient
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.core.script.DigestUtils
import org.springframework.util.Assert
import org.springframework.util.StringUtils

/**
 * @author lingting 2024-04-17 16:19
 */
class RepeatRedisScript<T> @JvmOverloads constructor(source: String, type: ReturnType = ReturnType.STATUS) {
    val source: String

    val sha1: String

    val type: ReturnType

    var isLoad: Boolean = false
        private set

    constructor(source: String, resultType: Class<T>) : this(source, ReturnType.fromJavaType(resultType))

    init {
        Assert.state(StringUtils.hasText(source), "Redis script source must not be empty")
        this.source = source
        this.sha1 = DigestUtils.sha1DigestAsHex(source)
        this.type = type
    }

    fun execute(keys: MutableList<String>, vararg args: Any): T? {
        return Redis.instance().scriptExecutor().execute<T>(this, keys, *args)
    }

    fun execute(connection: RedisConnection, keys: MutableList<String>, vararg args: Any): T? {
        return Redis.instance().scriptExecutor().execute<T>(connection, this, keys, *args)
    }

    fun load(client: RedissonClient) {
        val script = client.getScript()
        script.scriptLoad(source)
        this.isLoad = true
    }
}
