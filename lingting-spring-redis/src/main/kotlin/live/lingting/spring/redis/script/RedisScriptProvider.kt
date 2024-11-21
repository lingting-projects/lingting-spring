package live.lingting.spring.redis.script

/**
 * @author lingting 2024-04-17 19:00
 */
interface RedisScriptProvider {
    fun scripts(): Collection<RepeatRedisScript<*>>
}
