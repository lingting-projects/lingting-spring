package live.lingting.spring.redis;

import live.lingting.framework.util.CollectionUtils;
import live.lingting.framework.value.WaitValue;
import live.lingting.spring.redis.cache.RedisCache;
import live.lingting.spring.redis.properties.RedisProperties;
import live.lingting.spring.redis.script.RedisScriptExecutor;
import live.lingting.spring.redis.script.RedisScriptProvider;
import live.lingting.spring.redis.script.RepeatRedisScript;
import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.RedissonSpinLock;
import org.redisson.api.LockOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collection;
import java.util.List;

/**
 * @author lingting 2024-04-17 14:29
 */
@SuppressWarnings("unchecked")
public class Redis implements InitializingBean {

	private static final WaitValue<Redis> INSTANCE = new WaitValue<>();

	private final StringRedisTemplate template;

	private final Redisson redisson;

	private final RedisScriptExecutor<String> scriptExecutor;

	private final RedisProperties properties;

	public Redis(StringRedisTemplate template, Redisson redisson, RedisProperties properties,
			List<RedisScriptProvider> providers) {
		this.template = template;
		this.redisson = redisson;
		this.properties = properties;
		this.scriptExecutor = new RedisScriptExecutor<>(template);
		for (RedisScriptProvider provider : providers) {
			List<RepeatRedisScript<?>> scripts = provider.scripts();
			loadScripts(scripts);
		}
	}

	@SneakyThrows
	public static Redis instance() {
		return INSTANCE.notNull();
	}

	public StringRedisTemplate template() {
		return template;
	}

	public Redisson redisson() {
		return redisson;
	}

	public RedisScriptExecutor<String> scriptExecutor() {
		return scriptExecutor;
	}

	public RedisSerializer<String> keySerializer() {
		return (RedisSerializer<String>) template.getKeySerializer();
	}

	public RedisSerializer<String> valueSerializer() {
		return (RedisSerializer<String>) template.getValueSerializer();
	}

	public RedisSerializer<String> stringSerializer() {
		return template.getStringSerializer();
	}

	public HashOperations<String, String, String> hashOps() {
		return template.opsForHash();
	}

	public ValueOperations<String, String> valueOps() {
		return template.opsForValue();
	}

	public ListOperations<String, String> listOps() {
		return template.opsForList();
	}

	public SetOperations<String, String> setOps() {
		return template.opsForSet();
	}

	public ZSetOperations<String, String> zSetOps() {
		return template.opsForZSet();
	}

	public StreamOperations<String, String, String> streamOps() {
		return template.opsForStream();
	}

	public RedissonLock lock(String name) {
		return new RedissonLock(redisson.getCommandExecutor(), name);
	}

	public RedissonSpinLock spinLock(String name) {
		return new RedissonSpinLock(redisson.getCommandExecutor(), name, LockOptions.defaults());
	}

	public RedisCache cache() {
		return new RedisCache(this, properties.getNullValue(), properties.getCacheExpireTime(),
				properties.getLockTimeout(), properties.getLeaseTime());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE.update(this);
	}

	public void loadScripts(Collection<RepeatRedisScript<?>> scripts) {
		if (CollectionUtils.isEmpty(scripts)) {
			return;
		}
		for (RepeatRedisScript<?> script : scripts) {
			if (!script.isLoad()) {
				script.load(redisson);
			}
		}
	}

}
