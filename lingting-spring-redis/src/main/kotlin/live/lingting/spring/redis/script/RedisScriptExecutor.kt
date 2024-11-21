package live.lingting.spring.redis.script;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * @author lingting 2024-04-17 17:19
 */
@SuppressWarnings("unchecked")
public class RedisScriptExecutor<K> extends DefaultScriptExecutor<K> {

	protected final RedisTemplate<K, ?> template;

	public RedisScriptExecutor(RedisTemplate<K, ?> template) {
		super(template);
		this.template = template;
	}

	@Override
	public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer,
			List<K> keys, Object... args) {
		return template.execute((RedisCallback<T>) connection -> {
			ReturnType returnType = ReturnType.fromJavaType(script.getResultType());
			return eval(connection, returnType, argsSerializer, resultSerializer, scriptBytes(script), keys, args);
		});
	}

	public <T> T execute(RedisConnection connection, final RedisScript<T> script, final List<K> keys,
			final Object... args) {
		ReturnType returnType = ReturnType.fromJavaType(script.getResultType());
		return eval(connection, returnType, scriptBytes(script), keys, args);
	}

	public <T> T execute(RepeatRedisScript<T> script, List<K> keys, Object... args) {
		return template.execute((RedisCallback<T>) connection -> execute(connection, script, keys, args));
	}

	public <T> T execute(RedisConnection connection, RepeatRedisScript<T> script, List<K> keys, Object... args) {
		if (script.isLoad()) {
			return evalSha1(connection, script.getType(), script.getSha1(), keys, args);
		}
		RedisSerializer<String> serializer = template.getStringSerializer();
		byte[] bytes = serializer.serialize(script.getSource());
		return eval(connection, script.getType(), bytes, keys, args);
	}

	public <T> T eval(RedisConnection connection, ReturnType returnType, byte[] script, final List<K> keys,
			final Object... args) {
		return eval(connection, returnType, template.getValueSerializer(),
				(RedisSerializer<T>) template.getValueSerializer(), script, keys, args);
	}

	public <T> T eval(RedisConnection connection, ReturnType returnType, RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, byte[] script, List<K> keys, Object... args) {
		byte[][] keysAndArgs = keysAndArgs(argsSerializer, keys, args);
		int keySize = keys.size();
		RedisScriptingCommands commands = connection.scriptingCommands();
		Object result = commands.eval(script, returnType, keySize, keysAndArgs);
		if (connection.isQueueing() || connection.isPipelined() || result == null) {
			return null;
		}
		return deserializeResult(resultSerializer, result);
	}

	public <T> T evalSha1(RedisConnection connection, ReturnType returnType, String sha1, final List<K> keys,
			final Object... args) {
		return evalSha1(connection, returnType, template.getValueSerializer(),
				(RedisSerializer<T>) template.getValueSerializer(), sha1, keys, args);
	}

	public <T> T evalSha1(RedisConnection connection, ReturnType returnType, RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, String sha1, List<K> keys, Object... args) {
		byte[][] keysAndArgs = keysAndArgs(argsSerializer, keys, args);
		int keySize = keys.size();
		RedisScriptingCommands commands = connection.scriptingCommands();
		Object result = commands.evalSha(sha1, returnType, keySize, keysAndArgs);
		if (connection.isQueueing() || connection.isPipelined() || result == null) {
			return null;
		}
		return deserializeResult(resultSerializer, result);
	}

}
