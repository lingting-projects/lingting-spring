package live.lingting.spring.redis.configuration

import live.lingting.spring.redis.lock.RedisLock
import live.lingting.spring.redis.script.RedisScriptProvider
import live.lingting.spring.redis.script.RepeatRedisScript
import live.lingting.spring.redis.unique.RedisId

/**
 * @author lingting 2024/12/17 15:34
 */
class SpringRedisScriptProvider(
    beans: List<RepeatRedisScript<*>>
) : RedisScriptProvider {

    val scripts = mutableSetOf<RepeatRedisScript<*>>(
        RedisId.SCRIPT,
        RedisLock.SCRIPT_LOCK,
        RedisLock.SCRIPT_UNLOCK,
    ).apply { addAll(beans) }

    override fun scripts(): Collection<RepeatRedisScript<*>> {
        return scripts
    }
}
