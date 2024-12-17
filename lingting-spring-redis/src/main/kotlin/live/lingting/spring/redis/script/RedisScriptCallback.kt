package live.lingting.spring.redis.script

import org.springframework.data.redis.connection.RedisScriptingCommands

/**
 * @author lingting 2024/12/17 14:54
 */
fun interface RedisScriptCallback {

    fun apply(
        commands: RedisScriptingCommands,
        keySize: Int, keysAndArgs: Array<ByteArray>
    ): Any?

}
