package live.lingting.spring.util

import jakarta.annotation.Resource
import live.lingting.framework.util.ClassUtils
import live.lingting.framework.util.ClassUtils.constructors
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import java.lang.reflect.Constructor
import java.util.function.Function
import kotlin.reflect.KClass

/**
 * @author lingting 2022/10/15 15:21
 */
@Suppress("UNCHECKED_CAST")
object SpringUtils {

    @JvmField
    val AUTOWIRE_ANNOTATIONS = setOf(
        Autowired::class.java.name,
        Resource::class.java.name,
        "javax.annotation.Resource"
    )

    var context: ApplicationContext? = null

    @JvmStatic
    fun hasBean(name: String): Boolean {
        return context!!.containsBean(name)
    }

    @JvmStatic
    fun hasBean(cls: Class<*>): Boolean {
        try {
            val map = getBeansOfType(cls)
            return map.isNotEmpty()
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

    @JvmStatic
    fun <T> ofBean(cls: Class<T>, getArgument: Function<Class<*>, Any?>): T {
        val constructors: Array<Constructor<T>> = constructors<T>(cls)
        return ofBean<T>(constructors[0], getArgument)
    }

    fun <T : Any> ofBean(cls: KClass<T>): T = ofBean(cls.java)

    @JvmStatic
    fun <T : Any> ofBean(cls: KClass<T>, getArgument: Function<Class<*>, Any?>): T {
        return ofBean(cls.java, getArgument)
    }

    @JvmStatic
    fun <T> ofBean(constructor: Constructor<T>): T {
        return ofBean(constructor) { getBean(it) }
    }

    @JvmStatic
    fun <T> ofBean(constructor: Constructor<T>, getArgument: Function<Class<*>, Any?>): T {
        return ClassUtils.newInstance(constructor, getArg = getArgument)
    }

}
