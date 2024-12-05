package live.lingting.spring.util

import jakarta.annotation.Resource
import java.lang.reflect.Constructor
import java.util.function.Function
import kotlin.reflect.KClass
import live.lingting.framework.util.ClassUtils.classFields
import live.lingting.framework.util.ClassUtils.constructors
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

/**
 * @author lingting 2022/10/15 15:21
 */
@Suppress("UNCHECKED_CAST")
object SpringUtils {
    var context: ApplicationContext? = null

    @JvmStatic
    fun hasBean(name: String): Boolean {
        return context!!.containsBean(name)
    }

    @JvmStatic
    fun hasBean(cls: Class<*>): Boolean {
        try {
            val map = getBeansOfType(cls)
            return !map.isEmpty()
        } catch (_: NoSuchBeanDefinitionException) {
            return false
        }
    }

    fun hasBean(cls: KClass<*>) = hasBean(cls.java)

    @JvmStatic
    fun <T> getBean(name: String): T {
        return context!!.getBean(name) as T
    }

    @JvmStatic
    fun <T> getBean(clazz: Class<T>): T {
        return context!!.getBean<T>(clazz)
    }

    fun <T : Any> getBean(cls: KClass<T>): T = getBean(cls.java)

    @JvmStatic
    fun <T> getBean(name: String, clazz: Class<T>): T {
        return context!!.getBean<T>(name, clazz)
    }

    fun <T : Any> getBean(name: String, cls: KClass<T>): T = getBean(name, cls.java)

    @JvmStatic
    fun <T> getBeansOfType(type: Class<T>): Map<String, T> {
        return context!!.getBeansOfType<T>(type)
    }

    fun <T : Any> getBeansOfType(cls: KClass<T>): Map<String, T> = getBeansOfType(cls.java)

    @JvmStatic
    fun getBeanNamesForType(type: Class<*>): Array<String> {
        return context!!.getBeanNamesForType(type)
    }

    fun getBeanNamesForType(cls: KClass<*>): Array<String> = getBeanNamesForType(cls.java)

    @JvmStatic
    fun <T> ofBean(cls: Class<T>): T {
        val constructors: Array<Constructor<T>> = constructors<T>(cls)
        return ofBean<T>(constructors[0])
    }

    fun <T : Any> ofBean(cls: KClass<T>): T = ofBean(cls.java)

    @JvmStatic
    fun <T> ofBean(constructor: Constructor<T>): T {
        return ofBean(constructor) { getBean(it) }
    }

    @JvmStatic
    fun <T> ofBean(constructor: Constructor<T>, getArgument: Function<Class<*>, Any>): T {
        val types = constructor.parameterTypes
        val arguments: MutableList<Any> = ArrayList<Any>()

        for (cls in types) {
            val argument = getArgument.apply(cls)
            arguments.add(argument)
        }

        val t = constructor.newInstance(*arguments.toTypedArray())
        // 自动注入
        val cfs = classFields(t::class.java)
        for (cf in cfs) {
            if (!cf.hasField || cf.isFinalField) {
                continue
            }
            val isAutowired = cf.getAnnotation(Autowired::class.java) != null || cf.getAnnotation(Resource::class.java) != null
            if (isAutowired) {
                val cls = cf.valueType
                val arg = getArgument.apply(cls)
                cf.set(t as Any, arg)
            }
        }
        return t
    }

}
