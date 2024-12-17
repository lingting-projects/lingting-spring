package live.lingting.spring.redis.unique

import live.lingting.framework.id.Snowflake
import live.lingting.framework.lock.JavaReentrantLock
import live.lingting.framework.value.UniqueValue
import live.lingting.framework.value.WaitValue
import live.lingting.spring.redis.Redis
import live.lingting.spring.redis.script.RedisScriptExecutor
import live.lingting.spring.redis.script.RepeatRedisScript
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations

/**
 * @author lingting 2024/11/25 16:55
 */
class RedisId @JvmOverloads constructor(
    val template: RedisTemplate<String, String> = Redis.instance().template(),
    val executor: RedisScriptExecutor<List<*>> = RedisScriptExecutor(SCRIPT, template),
    val snowflake: Snowflake = Snowflake(0, 0),
    val key: String = "lingting:redis:unique:id"
) : UniqueValue<String> {

    companion object {

        val value = WaitValue<RedisId>()

        @JvmStatic
        fun instance() = value.notNull()

        @JvmStatic
        fun get() = instance().next()

        @JvmStatic
        fun get(count: Int) = instance().cursor(count)

        const val SCRIPT_LUA = """
local key = KEYS[1]
local count = tonumber(ARGV[1])
local result = {}

local values = redis.call('ZPOPMIN', key, count)

for i=1,#values,2 do
    local value = values[i]
    -- local score = values[i+1]
    table.insert(result, value)
end

return result
"""

        @JvmField
        val SCRIPT = RepeatRedisScript(SCRIPT_LUA, List::class.java)

    }

    val lock = JavaReentrantLock()

    /**
     * 一次最少生产多少个id
     */
    var min = 1000

    override fun next(): String = batch(1)[0]

    override fun batch(count: Int): List<String> {
        val ids = ArrayList<String>(count)

        while (count > ids.size) {
            val require = count - ids.size
            val batch = fromRedis(require)
            ids.addAll(batch)

            // 取出有数据, 继续
            if (!batch.isEmpty()) {
                continue
            }
            val zSet = template.opsForZSet()
            // 没有数据了, 往redis生成数据
            lock.runByInterruptibly {
                // 再次判断是否有数据
                val size = zSet.size(key)
                // 有数据了, 而且够了. 结束
                if (size != null && size > require) {
                    return@runByInterruptibly
                }

                val number = min.coerceAtLeast(require)
                val batch = snowflake.nextIds(number)
                val tuples = batch.map { ZSetOperations.TypedTuple<Any>.of(it.toString(), it.toDouble()) }.toSet()
                zSet.add(key, tuples)
            }
        }

        return ids
    }

    fun fromRedis(count: Int): List<String> {
        val keys = listOf(key)
        val execute = executor.execute(keys, count)
        if (execute.isNullOrEmpty()) {
            return emptyList()
        }

        return execute.filterNotNull().map { it.toString() }
    }

}
