package live.lingting.spring.redis.script

import com.fasterxml.jackson.databind.ObjectMapper
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.framework.jackson.wrapper.JacksonWrapper
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.redis.Redis
import org.springframework.data.redis.connection.RedisScriptingCommands
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer

/**
 * @author lingting 2024-04-17 17:19
 */
class RedisScriptExecutor<T> @JvmOverloads constructor(
    val script: RepeatRedisScript<T>,
    val template: RedisTemplate<*, *> = Redis.instance().template(),
    val jacksonWrapper: JacksonWrapper<ObjectMapper> = JacksonUtils.jsonWrapper
) {

    companion object {
        val log = logger()
    }

    /**
     * 脚本内容序列化
     */
    var bytesSerializer = template.stringSerializer as RedisSerializer<Any>

    /**
     * 脚本key序列化
     */
    var keySerializer = template.keySerializer as RedisSerializer<Any>

    /**
     * 脚本参数序列化
     */
    var argsSerializer = template.valueSerializer as RedisSerializer<Any>

    /**
     * 脚本返回值序列化
     */
    var resultSerializer = template.valueSerializer as RedisSerializer<Any>

    fun load() {
        template.execute(RedisCallback {
            val commands = it.scriptingCommands()
            load(commands)
        })
    }

    fun load(commands: RedisScriptingCommands) {
        if (script.load) {
            return
        }
        val bytes = bytes()
        commands.scriptLoad(bytes)
        script.load = true
    }

    fun bytes(): ByteArray {
        if (script.bytes == null) {
            script.bytes = bytesSerializer.serialize(script.source)
        }
        return script.bytes!!
    }

    @JvmOverloads
    fun execute(keys: List<Any> = emptyList(), vararg args: Any): T? {
        return execute(keys, args.toList())
    }

    @JvmOverloads
    fun execute(keys: List<Any> = emptyList(), args: Collection<Any> = emptyList()): T? {
        if (script.load) {
            return evalSha1(keys, args)
        }
        return eval(keys, args)
    }

    @JvmOverloads
    fun eval(keys: List<Any> = emptyList(), vararg args: Any): T? {
        return eval(keys, args.toList())
    }

    @JvmOverloads
    fun eval(keys: List<Any> = emptyList(), args: Collection<Any> = emptyList()): T? {
        return commands(keys, args) { c, s, v ->
            val bytes = bytes()
            val eval = c.eval<T>(bytes, script.type, s, *v)
            script.load = true
            eval
        }
    }

    @JvmOverloads
    fun evalSha1(keys: List<Any> = emptyList(), vararg args: Any): T? {
        return evalSha1(keys, args.toList())
    }

    @JvmOverloads
    fun evalSha1(keys: List<Any> = emptyList(), args: Collection<Any> = emptyList()): T? {
        return commands(keys, args) { c, s, v ->
            c.evalSha(script.sha1, script.type, s, *v)
        }
    }

    fun <T> convert(value: Any, target: Class<T>?): T {
        try {
            if (target != null) {
                val convert = jacksonWrapper.convert(value, target)
                if (convert != null) {
                    return convert
                }
            }
        } catch (e: Exception) {
            log.debug("Convert error! value: {}; target: {}", value, target, e)
        }
        return value as T
    }

    fun keysAndArgs(keys: List<Any>, args: Collection<Any>): Array<ByteArray> {
        val list = mutableListOf<ByteArray>()

        val keyType = keySerializer.targetType
        for (key in keys) {
            val value = convert(key, keyType)
            val bytes = keySerializer.serialize(value)
            list.add(bytes!!)
        }

        val argType = argsSerializer.targetType
        for (arg in args) {
            val value = convert(arg, argType)
            val bytes = argsSerializer.serialize(value)
            list.add(bytes!!)
        }

        return list.toTypedArray()
    }

    fun deserializeResult(result: Any?): T? {
        if (result == null) {
            return null
        }

        if (result is ByteArray) {
            return resultSerializer.deserialize(result) as T?
        }

        if (result is Iterable<*>) {
            val list = mutableListOf<Any?>()
            for (item in result) {
                val deserialize = deserializeResult(item)
                list.add(deserialize)
            }
            return list as T?
        }

        return result as T?
    }

    fun commands(keys: List<Any>, args: Collection<Any>, callback: RedisScriptCallback): T? {
        val keysAndArgs = keysAndArgs(keys, args)

        return template.execute(RedisCallback {
            val commands = it.scriptingCommands()
            val apply = callback.apply(commands, keys.size, keysAndArgs)
            if (it.isQueueing || it.isPipelined || apply == null) {
                return@RedisCallback null
            }
            deserializeResult(apply)
        })
    }

    fun <T> copy(script: RepeatRedisScript<T>) = RedisScriptExecutor(script, template).let {
        it.bytesSerializer = bytesSerializer
        it.keySerializer = keySerializer
        it.argsSerializer = argsSerializer
        it.resultSerializer = resultSerializer
        it
    }

}
