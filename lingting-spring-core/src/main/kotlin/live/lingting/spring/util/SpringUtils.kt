package live.lingting.spring.util

import jakarta.annotation.Resource
import java.lang.reflect.Constructor
import java.util.function.Function
import live.lingting.framework.util.ClassUtils.classFields
import live.lingting.framework.util.ClassUtils.constructors
import live.lingting.framework.util.CollectionUtils.isEmpty
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
            val map: MutableMap<String, *> = getBeansOfType(cls)
            return !isEmpty(map)
        } catch (e: NoSuchBeanDefinitionException) {
            return false
        }
    }

    @JvmStatic
    fun <T> getBean(name: String): T {
        return context!!.getBean(name) as T
    }

    @JvmStatic
    fun <T> getBean(clazz: Class<T>): T {
        return context!!.getBean<T>(clazz)
    }

    @JvmStatic
    fun <T> getBean(name: String, clazz: Class<T>): T {
        return context!!.getBean<T>(name, clazz)
    }

    @JvmStatic
    fun <T> getBeansOfType(type: Class<T>): MutableMap<String, T> {
        return context!!.getBeansOfType<T>(type)
    }

    @JvmStatic
    fun getBeanNamesForType(type: Class<*>): Array<String> {
        return context!!.getBeanNamesForType(type)
    }

    @JvmStatic
    fun <T> ofBean(cls: Class<T>): T {
        val constructors: Array<Constructor<T>> = constructors<T>(cls)
        return ofBean<T>(constructors[0], Function { clazz: Class<*> -> getBean(clazz) })
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
