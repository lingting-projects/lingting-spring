package live.lingting.spring.redis.script

/**
 * @author lingting 2025/8/1 15:27
 */
object RedisScripts {

    @JvmStatic
    val MULTI_SET_LUA = """-- 批量设置多个键为相同值，并可选设置过期时间
-- 解析过期时间（如果提供）
local ttl = nil
if ARGV[2] then
    ttl = tonumber(ARGV[2])
end

-- 为每个键设置值
for _, key in ipairs(KEYS) do
    -- 设置键值
    redis.call('SET', key, ARGV[1])

    -- 如果提供了过期时间，则设置
    if ttl then
        redis.call('EXPIRE', key, ttl)
    end
end"""

    @JvmStatic
    val MULTI_SET = RepeatRedisScript(MULTI_SET_LUA, Void::class.java)


}
