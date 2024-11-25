package live.lingting.spring.redis.unique

import live.lingting.framework.value.UniqueValue

/**
 * @author lingting 2024/11/25 16:54
 */
class RedisUniqueValue(val id: RedisId) : UniqueValue<String> {

    override fun next() = id.next()

    override fun batch(count: Int) = id.batch(count)

}
