package live.lingting.spring.redis.cache

import live.lingting.framework.util.CollectionUtils.multiToList
import live.lingting.framework.util.StringUtils.hasText
import live.lingting.spring.util.SpelUtils
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.lang.reflect.Method

/**
 * @author lingting 2024-04-18 10:04
 */
class CacheKeyGenerator(
    val delimiter: String,
    target: Any,
    method: Method,
    arguments: Array<Any>
) {
    val context: StandardEvaluationContext = SpelUtils.getSpelContext(target, method, arguments)

    fun resolve(spel: String): List<String> {
        if (!hasText(spel)) {
            return mutableListOf()
        }

        val obj = SpelUtils.parseValue(context, spel)
        return multiToList(obj).stream().map { it?.toString() ?: "" }.toList()
    }

    fun join(key: String, spel: String): String {
        val builder = StringBuilder(key)

        val list = resolve(spel)

        if (!list.isEmpty()) {
            for (s in list) {
                builder.append(delimiter).append(s)
            }
        }

        return builder.toString()
    }

    fun multi(key: String, spel: String): MutableList<String> {
        val list = resolve(spel)
        if (list.isEmpty()) {
            return mutableListOf(key)
        }

        val strings = ArrayList<String>(list.size)

        for (s in list) {
            strings.add("$key$delimiter$s")
        }

        return strings
    }

    fun cached(a: Cached?): MutableList<String> {
        if (a == null) {
            return mutableListOf()
        }
        if (a.keyMulti) {
            return multi(a.key, a.keyJoint)
        }
        return mutableListOf(join(a.key, a.keyJoint))
    }

    fun cached(a: Cached?, batch: CachedBatch?): List<String> {
        val key = cached(a)
        batch?.let {
            for (ba in it.value) {
                key.addAll(cached(ba))
            }
        }
        return key
    }

    fun cacheClear(a: CacheClear?): MutableList<String> {
        if (a == null) {
            return mutableListOf()
        }
        if (a.keyMulti) {
            return multi(a.key, a.keyJoint)
        }
        return mutableListOf(join(a.key, a.keyJoint))
    }

    fun cacheClear(a: CacheClear?, batch: CacheClearBatch?): List<String> {
        val key = cacheClear(a)
        batch?.let {
            for (ba in it.value) {
                key.addAll(cacheClear(ba))
            }
        }
        return key
    }
}
