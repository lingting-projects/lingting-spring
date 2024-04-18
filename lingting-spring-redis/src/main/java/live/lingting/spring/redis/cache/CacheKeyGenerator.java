package live.lingting.spring.redis.cache;

import live.lingting.framework.util.CollectionUtils;
import live.lingting.framework.util.StringUtils;
import live.lingting.spring.util.SpelUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lingting 2024-04-18 10:04
 */
public class CacheKeyGenerator {

	protected final String delimiter;

	protected final StandardEvaluationContext context;

	public CacheKeyGenerator(String delimiter, Object target, Method method, Object[] arguments) {
		this.delimiter = delimiter;
		this.context = SpelUtils.getSpelContext(target, method, arguments);
	}

	public List<String> resolve(String spel) {
		if (!StringUtils.hasText(spel)) {
			return Collections.emptyList();
		}

		Object obj = SpelUtils.parseValue(context, spel);
		return CollectionUtils.multiToList(obj).stream().map(o -> o == null ? "" : o.toString()).toList();
	}

	public String join(String key, String spel) {
		StringBuilder builder = new StringBuilder(key);

		List<String> list = resolve(spel);

		if (!CollectionUtils.isEmpty(list)) {
			for (String s : list) {
				builder.append(delimiter).append(s);
			}
		}

		return builder.toString();
	}

	public List<String> multi(String key, String spel) {
		List<String> list = resolve(spel);
		if (!CollectionUtils.isEmpty(list)) {
			return Collections.singletonList(key);
		}

		List<String> strings = new ArrayList<>(list.size());

		for (String s : list) {
			strings.add("%s%s%s".formatted(key, delimiter, s));
		}

		return strings;
	}

	public String cached(Cached cached) {
		if (cached == null) {
			return "";
		}
		return join(cached.key(), cached.keyJoint());
	}

	public List<String> cacheClear(CacheClear clear) {
		if (clear == null) {
			return Collections.emptyList();
		}
		if (clear.multi()) {
			return multi(clear.key(), clear.keyJoint());
		}
		return Collections.singletonList(join(clear.key(), clear.keyJoint()));
	}

	public List<String> cacheClear(CacheClear clear, CacheBatchClear batchClear) {
		List<String> strings = new ArrayList<>(cacheClear(clear));
		for (CacheClear cacheClear : batchClear.value()) {
			strings.addAll(cacheClear(cacheClear));
		}
		return strings;
	}

}
