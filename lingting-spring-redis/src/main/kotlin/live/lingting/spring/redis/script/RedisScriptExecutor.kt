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
@Suppress("UNCHECKED_CAST", "WRONG_NULLABILITY_FOR_JAVA_OVERRIDE", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RedisScriptExecutor<K>(val template: RedisTemplate<K, String>) : DefaultScriptExecutor<K>(template) {

    fun load(script: RepeatRedisScript<*>) {
        template.execute(RedisCallback {
            val commands = it.scriptingCommands()
            val bytes = scriptBytes(script)
            commands.scriptLoad(bytes)
        })
    }

    fun scriptBytes(script: RepeatRedisScript<*>): ByteArray {
        val serializer = template.stringSerializer
        return serializer.serialize(script.source)
    }

    override fun keysAndArgs(argsSerializer: RedisSerializer<*>, keys: List<K?>?, args: Array<out Any?>): Array<out ByteArray?> {
        val map = args.map { it?.toString() }
        return super.keysAndArgs(argsSerializer, keys, map.toTypedArray())
    }

    override fun <T : Any> execute(
        script: RedisScript<T>,
        argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>,
        keys: List<K>?,
        vararg args: Any
    ): T? {
        return template.execute<T>(RedisCallback { connection ->
            val returnType = ReturnType.fromJavaType(script.getResultType())
            val bytes = scriptBytes(script)
            eval<T>(connection, returnType, argsSerializer, resultSerializer, bytes, keys, *args)
        })
    }

    fun <T : Any> execute(
        connection: RedisConnection, script: RedisScript<T>, keys: List<K>?,
        vararg args: Any
    ): T? {
        val returnType = ReturnType.fromJavaType(script.getResultType())
        val bytes = scriptBytes(script)
        return eval<T>(connection, returnType, bytes, keys, *args)
    }

    fun <T : Any> execute(script: RepeatRedisScript<T>, keys: List<K>?, vararg args: Any): T? {
        return template.execute<T>(RedisCallback { connection -> execute<T>(connection, script, keys, *args) })
    }

    fun <T : Any> execute(script: RepeatRedisScript<T>, keys: List<K>?, args: Collection<Any>): T? {
        return execute(script, keys, *args.toTypedArray())
    }

    fun <T : Any> execute(connection: RedisConnection, script: RepeatRedisScript<T>, keys: List<K>?, vararg args: Any): T? {
        if (script.isLoad) {
            return evalSha1<T>(connection, script.type, script.sha1, keys, *args)
        }
        val bytes = scriptBytes(script)
        return eval<T>(connection, script.type, bytes, keys, *args)
    }

    fun <T : Any> execute(connection: RedisConnection, script: RepeatRedisScript<T>, keys: List<K>?, args: Collection<Any>): T? {
        return execute(connection, script, keys, *args.toTypedArray())
    }

    fun <T : Any> eval(
        connection: RedisConnection, returnType: ReturnType, script: ByteArray, keys: List<K>?,
        vararg args: Any
    ): T? {
        return eval<T>(
            connection, returnType, template.valueSerializer,
            template.valueSerializer as RedisSerializer<T>, script, keys, *args
        )
    }

    fun <T : Any> eval(
        connection: RedisConnection, returnType: ReturnType, script: ByteArray, keys: List<K>?,
        args: Collection<Any>
    ): T? {
        return eval<T>(connection, returnType, script, keys, *args.toTypedArray())
    }

    fun <T : Any> eval(
        connection: RedisConnection, returnType: ReturnType, argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>, script: ByteArray, keys: List<K>?, vararg args: Any
    ): T? {
        val keysAndArgs = keysAndArgs(argsSerializer, keys, args)
        val keySize = keys?.size ?: 0
        val commands = connection.scriptingCommands()
        val result = commands.eval<Any>(script, returnType, keySize, *keysAndArgs)
        if (connection.isQueueing || connection.isPipelined || result == null) {
            return null
        }
        return deserializeResult<T>(resultSerializer, result)
    }

    fun <T : Any> eval(
        connection: RedisConnection, returnType: ReturnType, argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>, script: ByteArray, keys: List<K>?, args: Collection<Any>
    ): T? {
        return eval<T>(connection, returnType, argsSerializer, resultSerializer, script, keys, *args.toTypedArray())
    }


    fun <T : Any> evalSha1(
        connection: RedisConnection, returnType: ReturnType, sha1: String, keys: List<K>?,
        vararg args: Any
    ): T? {
        return evalSha1<T>(
            connection, returnType, template.valueSerializer,
            template.valueSerializer as RedisSerializer<T>, sha1, keys, *args
        )
    }

    fun <T : Any> evalSha1(connection: RedisConnection, script: RepeatRedisScript<T>, keys: List<K>?, vararg args: Any): T? {
        return evalSha1(connection, script.type, script.sha1, keys, *args)
    }

    fun <T : Any> evalSha1(
        connection: RedisConnection, returnType: ReturnType, argsSerializer: RedisSerializer<*>,
        resultSerializer: RedisSerializer<T>, sha1: String, keys: List<K>?, vararg args: Any
    ): T? {
        val keysAndArgs = keysAndArgs(argsSerializer, keys, args)
        val keySize = keys?.size ?: 0
        val commands = connection.scriptingCommands()
        val result = commands.evalSha<Any>(sha1, returnType, keySize, *keysAndArgs)
        if (connection.isQueueing || connection.isPipelined || result == null) {
            return null
        }
        return deserializeResult<T>(resultSerializer, result)
    }

    fun <T : Any> evalSha1(connection: RedisConnection, script: RepeatRedisScript<T>, keys: List<K>?, args: Collection<Any>): T? {
        return evalSha1(connection, script.type, script.sha1, keys, *args.toTypedArray())
    }

}
