package live.lingting.spring.redis.script;

import live.lingting.spring.redis.Redis;
import lombok.Getter;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author lingting 2024-04-17 16:19
 */
@Getter
public class RepeatRedisScript<T> {

	private final String source;

	private final String sha1;

	private final ReturnType type;

	private boolean load = false;

	public RepeatRedisScript(String source) {
		this(source, ReturnType.STATUS);
	}

	public RepeatRedisScript(String source, Class<T> resultType) {
		this(source, ReturnType.fromJavaType(resultType));
	}

	public RepeatRedisScript(String source, ReturnType type) {
		Assert.state(StringUtils.hasText(source), "Redis script source must not be empty");
		this.source = source;
		this.sha1 = DigestUtils.sha1DigestAsHex(source);
		this.type = type;
	}

	public T execute(List<String> keys, Object... args) {
		return Redis.instance().scriptExecutor().execute(this, keys, args);
	}

	public T execute(RedisConnection connection, List<String> keys, Object... args) {
		return Redis.instance().scriptExecutor().execute(connection, this, keys, args);
	}

	public void load(RedissonClient client) {
		RScript script = client.getScript();
		script.scriptLoad(source);
		this.load = true;
	}

}
