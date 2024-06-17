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
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
		return cache(properties.getCacheExpireTime(), properties.getLockTimeout(), properties.getLeaseTime());
	}

	public RedisCache cache(Duration expireTime, Duration lockTimeout, Duration leaseTime) {
		return new RedisCache(this, properties.getNullValue(), expireTime, lockTimeout, leaseTime);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE.update(this);
	}

	// region script
	public void loadScript(RepeatRedisScript<?> script) {
		if (!script.isLoad()) {
			script.load(redisson);
		}
	}

	public void loadScripts(Collection<RepeatRedisScript<?>> scripts) {
		if (CollectionUtils.isEmpty(scripts)) {
			return;
		}
		for (RepeatRedisScript<?> script : scripts) {
			loadScript(script);
		}
	}

	// endregion

	// region key

	/**
	 * 删除指定的 key
	 * @param key 要删除的 key
	 * @return 删除成功返回 true, 如果 key 不存在则返回 false
	 * @see <a href="http://redis.io/commands/del">Del Command</a>
	 */
	public boolean delete(String key) {
		return Boolean.TRUE.equals(template().delete(key));
	}

	/**
	 * 删除指定的 keys
	 * @param keys 要删除的 key 数组
	 * @return 如果删除了一个或多个 key，则为大于 0 的整数，如果指定的 key 都不存在，则为 0
	 */
	public long delete(String... keys) {
		return delete(Arrays.asList(keys));
	}

	public long delete(Collection<String> keys) {
		Long l = template().delete(keys);
		return l == null ? 0 : l;
	}

	/**
	 * 判断 key 是否存在
	 * @param key 待判断的 key
	 * @return 如果 key 存在 {@code true} , 否则返回 {@code false}
	 * @see <a href="http://redis.io/commands/exists">Exists Command</a>
	 */
	public boolean exists(String key) {
		return Boolean.TRUE.equals(template().hasKey(key));
	}

	/**
	 * 判断指定的 key 是否存在.
	 * @param keys 待判断的数组
	 * @return 指定的 keys 在 redis 中存在的的数量
	 * @see <a href="http://redis.io/commands/exists">Exists Command</a>
	 */
	public long exists(String... keys) {
		return exists(Arrays.asList(keys));
	}

	public long exists(Collection<String> keys) {
		Long l = template().countExistingKeys(keys);
		return l == null ? 0 : l;
	}

	/**
	 * 设置过期时间
	 * @param key 待修改过期时间的 key
	 * @param timeout 过期时长，单位 秒
	 * @see <a href="http://redis.io/commands/expire">Expire Command</a>
	 */
	public boolean expire(String key, long timeout) {
		return expire(key, timeout, TimeUnit.SECONDS);
	}

	public boolean expire(String key, Duration timeout) {
		return expire(key, timeout.toSeconds(), TimeUnit.SECONDS);
	}

	/**
	 * 设置过期时间
	 * @param key 待修改过期时间的 key
	 * @param timeout 时长
	 * @param timeUnit 时间单位
	 */
	public boolean expire(String key, long timeout, TimeUnit timeUnit) {
		return Boolean.TRUE.equals(template().expire(key, timeout, timeUnit));
	}

	/**
	 * 设置 key 的过期时间到指定的日期
	 * @param key 待修改过期时间的 key
	 * @param date 过期时间
	 * @return 修改成功返回 true
	 * @see <a href="https://redis.io/commands/expireat/">ExpireAt Command</a>
	 */
	public boolean expireAt(String key, Date date) {
		return Boolean.TRUE.equals(template().expireAt(key, date));
	}

	public boolean expireAt(String key, Instant expireAt) {
		return Boolean.TRUE.equals(template().expireAt(key, expireAt));
	}

	/**
	 * 获取所有符合指定表达式的 key
	 * @param pattern 表达式
	 * @return java.util.Set<java.lang.String>
	 * @see <a href="http://redis.io/commands/keys">Keys Command</a>
	 */
	public Set<String> keys(String pattern) {
		return template().keys(pattern);
	}

	/**
	 * TTL 命令返回 {@link this#expire(String, long) EXPIRE} 命令设置的剩余生存时间（以秒为单位）.。
	 * <p>
	 * 时间复杂度: O(1)
	 * @param key 待查询的 key
	 * @return TTL 以秒为单位，或负值以指示错误
	 * @see <a href="http://redis.io/commands/ttl">TTL Command</a>
	 */
	public long ttl(String key) {
		Long l = template().getExpire(key);
		return l == null ? 0 : l;
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param scanOptions scan 的配置
	 * @return Cursor，一个可迭代对象
	 * @see <a href="https://redis.io/commands/scan/">Scan Command</a>
	 */
	public Cursor<String> scan(ScanOptions scanOptions) {
		return template().scan(scanOptions);
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param patten key 的规则
	 * @return Cursor，一个可迭代对象
	 */
	public Cursor<String> scan(String patten) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(patten).build();
		return scan(scanOptions);
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param patten key 的规则
	 * @param count 一次扫描获取的 key 数量， 默认为 10
	 * @return Cursor，一个可迭代对象
	 * @see <a href="https://redis.io/commands/scan/">Scan Command</a>
	 */
	public Cursor<String> scan(String patten, long count) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(patten).count(count).build();
		return scan(scanOptions);
	}
	// endregion

	// region string

	/**
	 * 当 key 存在时，对其值进行自减操作 （自减步长为 1），当 key 不存在时，则先赋值为 0 再进行自减
	 * @param key key
	 * @return 自减之后的 value 值
	 * @see this#decrement(String, long)
	 */
	public long decrement(String key) {
		Long l = valueOps().decrement(key);
		return l == null ? 0 : l;
	}

	/**
	 * 当 key 存在时，对其值进行自减操作，当 key 不存在时，则先赋值为 0 再进行自减
	 * @param key key
	 * @param delta 自减步长
	 * @return 自减之后的 value 值
	 * @see <a href="http://redis.io/commands/decrby">DecrBy Command</a>
	 */
	public long decrement(String key, long delta) {
		Long l = valueOps().decrement(key, delta);
		return l == null ? 0 : l;
	}

	/**
	 * 获取指定 key 的 value 值
	 * @param key 指定的 key
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/get">Get Command</a>
	 */
	public String get(String key) {
		return valueOps().get(key);
	}

	/**
	 * 获取指定 key 的 value 值，并将指定的 key 进行删除
	 * @param key 指定的 key
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getdel/">GetDel Command</a>
	 */
	public String getAndDelete(String key) {
		return valueOps().getAndDelete(key);
	}

	/**
	 * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
	 * @param key 指定的 key
	 * @param timeout 过期时间，单位时间秒
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getex/">GetEx Command</a>
	 */
	public String getAndExpire(String key, long timeout) {
		return getAndExpire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
	 * @param key 指定的 key
	 * @param timeout 过期时间，单位时间秒
	 * @param timeUnit 时间单位
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getex/">GetEx Command</a>
	 */
	public String getAndExpire(String key, long timeout, TimeUnit timeUnit) {
		return valueOps().getAndExpire(key, timeout, timeUnit);
	}

	/**
	 * 获取指定的 key 的 value 值，并同时使用指定的 value 值进行覆盖操作
	 * @param key 指定的 key
	 * @param value 新的 value 值
	 * @return 当 key 存在时返回其 value 值，否则返回 null
	 * @see <a href="http://redis.io/commands/getset">GetSet Command</a>
	 */
	public String getAndSet(String key, String value) {
		return valueOps().getAndSet(key, value);
	}

	/**
	 * 对 key 进行自增，自增步长为 1
	 * @param key 需要自增的 key
	 * @return 自增后的 value 值
	 * @see this#incrementBy(String, long)
	 */
	public long increment(String key) {
		Long l = valueOps().increment(key);
		return l == null ? 0 : l;
	}

	/**
	 * 对 key 进行自增，并指定自增步长, 当 key 不存在时先创建一个值为 0 的 key，再进行自增
	 * @param key 需要自增的 key
	 * @param delta 自增的步长
	 * @return 自增后的 value 值
	 * @see <a href="http://redis.io/commands/incrby">IncrBy Command</a>
	 */
	public long incrementBy(String key, long delta) {
		Long l = valueOps().increment(key, delta);
		return l == null ? 0 : l;
	}

	/**
	 * @see this#incrementBy(String, long)
	 */
	public double incrementByFloat(String key, double delta) {
		Double d = valueOps().increment(key, delta);
		return d == null ? 0 : d;
	}

	/**
	 * 从指定的 keys 批量获取 values
	 * @param keys keys
	 * @return values list，当值为空时，该 key 对应的 value 为 null
	 * @see <a href="http://redis.io/commands/mget">MGet Command</a>
	 */
	public List<String> multiGet(Collection<String> keys) {
		return valueOps().multiGet(keys);
	}

	/**
	 * @see this#multiGet(Collection)
	 */
	public List<String> multiGet(String... keys) {
		return multiGet(Arrays.asList(keys));
	}

	/**
	 * 批量获取 keys 的值，并返回一个 map
	 * @param keys keys
	 * @return map，key 和 value 的键值对集合，当 value 获取为 null 时，不存入此 map
	 */
	public Map<String, String> multiGetMap(Collection<String> keys) {
		return multiGetMap(keys, t -> t);
	}

	/**
	 * 获取多个key 并且值组装为map
	 * @param keys key
	 * @param convert 值序列化方法
	 * @return java.util.Map<java.lang.String, T> map，key 和 value 的键值对集合，当 value 获取为 null
	 * 时，不存入此 map
	 */
	public <T> Map<String, T> multiGetMap(Collection<String> keys, Function<String, T> convert) {
		Map<String, T> map = new HashMap<>();
		if (CollectionUtils.isEmpty(keys)) {
			return map;
		}
		List<String> values = valueOps().multiGet(keys);
		if (values == null || CollectionUtils.isEmpty(values)) {
			return map;
		}
		Iterator<String> keysIterator = keys.iterator();
		Iterator<String> valuesIterator = values.iterator();
		while (keysIterator.hasNext()) {
			String key = keysIterator.next();
			String value = valuesIterator.next();
			if (value != null) {
				T t = convert.apply(value);
				map.put(key, t);
			}
		}

		return map;
	}

	/**
	 * @see this#multiGetMap(Collection)
	 */
	public Map<String, String> multiGetMap(String... keys) {
		return multiGetMap(Arrays.asList(keys));
	}

	/**
	 * 设置 value for key
	 * @param key 指定的 key
	 * @param value 值
	 * @see <a href="https://redis.io/commands/set">Set Command</a>
	 */
	public void set(String key, String value) {
		valueOps().set(key, value);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间 单位：秒
	 * @see this#set(String, String, long)
	 */
	public void set(String key, String value, long timeout) {
		set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间 单位：秒
	 * @param timeUnit 过期时间单位
	 * @see this#set(String, String, long, TimeUnit)
	 */
	public void set(String key, String value, long timeout, TimeUnit timeUnit) {
		valueOps().set(key, value, timeout, timeUnit);
	}

	/**
	 * 设置 value for key, 同时为其设置其在指定时间过期
	 * @param key key
	 * @param value value
	 * @param expireTime 在指定时间过期
	 */
	public void set(String key, String value, Instant expireTime) {
		long timeout = expireTime.getEpochSecond() - Instant.now().getEpochSecond();
		set(key, value, timeout);
	}

	public void set(String key, String value, Duration duration) {
		set(key, value, duration.toSeconds());
	}

	/**
	 * 当 key 不存在时，进行 value 设置，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @return boolean
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public boolean setIfAbsent(String key, String value) {
		return Boolean.TRUE.equals(valueOps().setIfAbsent(key, value));
	}

	public boolean setIfAbsent(String key, String value, Duration duration) {
		return Boolean.TRUE.equals(valueOps().setIfAbsent(key, value, duration));
	}

	/**
	 * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @return boolean 操作是否成功
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public boolean setIfAbsent(String key, String value, long timeout) {
		return setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @param timeUnit 时间单位
	 * @return boolean 操作是否成功
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
		return Boolean.TRUE.equals(valueOps().setIfAbsent(key, value, timeout, timeUnit));
	}

	// endregion

}
