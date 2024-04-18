package live.lingting.spring.redis.script;

import java.util.List;

/**
 * @author lingting 2024-04-17 19:00
 */
public interface RedisScriptProvider {

	List<RepeatRedisScript<?>> scripts();

}
