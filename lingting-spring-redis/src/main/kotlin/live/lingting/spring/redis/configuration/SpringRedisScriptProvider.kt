package live.lingting.spring.redis.configuration

import live.lingting.framework.util.ClassUtils
import live.lingting.framework.util.FieldUtils.isPublic
import live.lingting.framework.util.FieldUtils.isStatic
import live.lingting.spring.redis.lock.RedisLock
import live.lingting.spring.redis.script.RedisScriptProvider
import live.lingting.spring.redis.script.RedisScripts
import live.lingting.spring.redis.script.RepeatRedisScript
import live.lingting.spring.redis.unique.RedisId

/**
 * @author lingting 2024/12/17 15:34
 */
class SpringRedisScriptProvider(beans: List<RepeatRedisScript<*>>) : RedisScriptProvider {

    val innerScripts = ClassUtils.fields(RedisScripts::class.java).filter {
        it.isPublic && it.isStatic && RepeatRedisScript::class.java.isAssignableFrom(it.type)
    }.mapNotNull { it.get(RedisScripts) as RepeatRedisScript<*>? }

    val scripts = mutableSetOf<RepeatRedisScript<*>>(
        RedisId.SCRIPT,
        RedisLock.SCRIPT_LOCK,
        RedisLock.SCRIPT_UNLOCK,
    ).apply {
        addAll(innerScripts)
        addAll(beans)
    }

    override fun scripts(): Collection<RepeatRedisScript<*>> {
        return scripts
    }
}
