package live.lingting.spring.redis.cache

import java.lang.reflect.Method
import live.lingting.framework.util.CollectionUtils.isEmpty
import live.lingting.framework.util.CollectionUtils.multiToList
import live.lingting.framework.util.StringUtils.hasText
import live.lingting.spring.util.SpelUtils
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * @author lingting 2024-04-18 10:04
 */
class CacheKeyGenerator(protected val delimiter: String, target: Any, method: Method, arguments: Array<Any>) {
    protected val context: StandardEvaluationContext

    init {
        this.context = SpelUtils.getSpelContext(target!!, method!!, arguments!!)
    }

    fun resolve(spel: String): MutableList<String> {
        if (!hasText(spel)) {
            return mutableListOf<String>()
        }

        val obj = SpelUtils.parseValue(context, spel!!)
        return multiToList(obj).stream().map<String> { o -> if (o == null) "" else o.toString() }.toList()
    }

    fun join(key: String, spel: String): String {
        val builder = StringBuilder(key)

        val list = resolve(spel)

        if (!isEmpty(list)) {
            for (s in list) {
                builder.append(delimiter).append(s)
            }
        }

        return builder.toString()
    }

    fun multi(key: String, spel: String): MutableList<String> {
        val list = resolve(spel)
        if (!isEmpty(list)) {
            return mutableListOf<String>(key)
        }

        val strings: MutableList<String> = ArrayList<String>(list.size)

        for (s in list) {
            strings.add("$key$delimiter$s")
        }

        return strings
    }

    fun cached(cached: Cached?): String {
        if (cached == null) {
            return ""
        }
        return join(cached.key, cached.keyJoint)
    }

    fun cacheClear(clear: CacheClear?): MutableList<String> {
        if (clear == null) {
            return mutableListOf<String>()
        }
        if (clear.multi) {
            return multi(clear.key, clear.keyJoint)
        }
        return mutableListOf<String>(join(clear.key, clear.keyJoint))
    }

    fun cacheClear(clear: CacheClear, batchClear: CacheBatchClear): MutableList<String> {
        val strings: MutableList<String> = ArrayList<String>(cacheClear(clear))
        for (cacheClear in batchClear.value) {
            strings.addAll(cacheClear(cacheClear))
        }
        return strings
    }
}
