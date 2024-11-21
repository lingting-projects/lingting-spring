package live.lingting.spring.redis.script

import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultScriptExecutor
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.data.redis.serializer.RedisSerializer

/**
 * @author lingting 2024-04-17 17:19
 */
class RedisScriptExecutor<K>(protected val template: RedisTemplate<K, *>) : DefaultScriptExecutor<K>(template) {
    override fun <T> execute(
        script: RedisScript<T>,
        argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>,
        keys: MutableList<K>,
        vararg args: Any
    ): T? {
        return template.execute<T>(RedisCallback { connection ->
            val returnType = ReturnType.fromJavaType(script.getResultType())
            eval<T>(connection, returnType, argsSerializer, resultSerializer, scriptBytes(script), keys, *args)
        })
    }

    fun <T> execute(
        connection: RedisConnection, script: RedisScript<T>, keys: MutableList<K>,
        vararg args: Any
    ): T? {
        val returnType = ReturnType.fromJavaType(script.getResultType())
        return eval<T>(connection, returnType, scriptBytes(script), keys, *args)
    }

    fun <T> execute(script: RepeatRedisScript<T>, keys: MutableList<K>, vararg args: Any): T? {
        return template.execute<T>(RedisCallback { connection -> execute<T>(connection, script, keys, *args) })
    }

    fun <T> execute(connection: RedisConnection, script: RepeatRedisScript<T>, keys: MutableList<K>, vararg args: Any): T? {
        if (script.isLoad) {
            return evalSha1<T>(connection, script.type, script.sha1, keys, *args)
        }
        val serializer = template.getStringSerializer()
        val bytes = serializer.serialize(script.source)
        return eval<T>(connection, script.type, bytes!!, keys, *args)
    }

    fun <T> eval(
        connection: RedisConnection, returnType: ReturnType, script: ByteArray, keys: MutableList<K>,
        vararg args: Any
    ): T? {
        return eval<T>(
            connection, returnType, template.valueSerializer,
            template.valueSerializer as RedisSerializer<T>, script, keys, *args
        )
    }

    fun <T> eval(
        connection: RedisConnection, returnType: ReturnType, argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>, script: ByteArray, keys: MutableList<K>, vararg args: Any
    ): T? {
        val keysAndArgs = keysAndArgs(argsSerializer, keys, args)
        val keySize = keys.size
        val commands = connection.scriptingCommands()
        val result = commands.eval<Any>(script, returnType, keySize, *keysAndArgs)
        if (connection.isQueueing || connection.isPipelined || result == null) {
            return null
        }
        return deserializeResult<T>(resultSerializer, result)
    }

    fun <T> evalSha1(
        connection: RedisConnection, returnType: ReturnType, sha1: String, keys: MutableList<K>,
        vararg args: Any
    ): T? {
        return evalSha1<T>(
            connection, returnType, template.getValueSerializer(),
            template.valueSerializer as RedisSerializer<T>, sha1, keys, *args
        )
    }

    fun <T> evalSha1(
        connection: RedisConnection, returnType: ReturnType, argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>, sha1: String, keys: MutableList<K>, vararg args: Any
    ): T? {
        val keysAndArgs = keysAndArgs(argsSerializer, keys, args)
        val keySize = keys.size
        val commands = connection.scriptingCommands()
        val result = commands.evalSha<Any>(sha1, returnType, keySize, *keysAndArgs)
        if (connection.isQueueing() || connection.isPipelined() || result == null) {
            return null
        }
        return deserializeResult<T>(resultSerializer, result)
    }
}
