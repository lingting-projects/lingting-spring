package live.lingting.spring.util

import java.util.Optional
import java.util.function.Function
import live.lingting.framework.util.ArrayUtils.containsIgnoreCase
import org.springframework.core.convert.support.ConfigurableConversionService
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.Profiles

/**
 * @author lingting 2022/10/15 11:33
 */
object EnvironmentUtils {
    @JvmStatic
    var environment: ConfigurableEnvironment? = null

    @JvmStatic
    fun containsProperty(key: String): Boolean {
        return environment!!.containsProperty(key)
    }

    @JvmStatic
    fun getProperty(key: String): String {
        return environment!!.getProperty(key)
    }

    @JvmStatic
    fun getProperty(key: String, defaultValue: String): String {
        return environment!!.getProperty(key, defaultValue)
    }

    @JvmStatic
    fun <T> getProperty(key: String, targetType: Class<T>): T {
        return environment!!.getProperty<T>(key, targetType)
    }

    @JvmStatic
    fun <T : Any> getProperty(key: String, targetType: Class<T>, defaultValue: T): T {
        return environment!!.getProperty<T>(key, targetType, defaultValue)
    }


    @JvmStatic
    fun getRequiredProperty(key: String): String {
        return environment!!.getRequiredProperty(key)
    }

    @JvmStatic
    fun <T> getRequiredProperty(key: String, targetType: Class<T>): T {
        return environment!!.getRequiredProperty<T>(key, targetType)
    }

    @JvmStatic
    fun resolvePlaceholders(text: String): String {
        return environment!!.resolvePlaceholders(text)
    }

    @JvmStatic
    fun resolveRequiredPlaceholders(text: String): String {
        return environment!!.resolveRequiredPlaceholders(text)
    }

    @JvmStatic
    fun addActiveProfile(profile: String) {
        environment!!.addActiveProfile(profile)
    }

    @JvmStatic
    val propertySources: MutablePropertySources
        get() = environment!!.propertySources

    @JvmStatic
    val systemProperties: MutableMap<String, Any>
        get() = environment!!.systemProperties

    @JvmStatic
    val systemEnvironment: MutableMap<String, Any>
        get() = environment!!.systemEnvironment

    @JvmStatic
    fun merge(parent: ConfigurableEnvironment) {
        environment!!.merge(parent)
    }

    @JvmStatic
    var conversionService: ConfigurableConversionService
        get() = environment!!.conversionService
        set(conversionService) {
            environment!!.setConversionService(conversionService)
        }

    @JvmStatic
    fun setPlaceholderPrefix(placeholderPrefix: String) {
        environment!!.setPlaceholderPrefix(placeholderPrefix)
    }

    @JvmStatic
    fun setPlaceholderSuffix(placeholderSuffix: String) {
        environment!!.setPlaceholderSuffix(placeholderSuffix)
    }

    @JvmStatic
    fun setValueSeparator(valueSeparator: String) {
        environment!!.setValueSeparator(valueSeparator)
    }

    @JvmStatic
    fun setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders: Boolean) {
        environment!!.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders)
    }

    @JvmStatic
    fun setRequiredProperties(vararg requiredProperties: String) {
        environment!!.setRequiredProperties(*requiredProperties)
    }


    @JvmStatic
    fun validateRequiredProperties() {
        environment!!.validateRequiredProperties()
    }

    val activeProfiles: Array<String>
        get() = environment!!.activeProfiles

    @JvmStatic
    fun setActiveProfiles(vararg profiles: String) {
        environment!!.setActiveProfiles(*profiles)
    }

    val defaultProfiles: Array<String>
        get() = environment!!.defaultProfiles

    @JvmStatic
    fun setDefaultProfiles(vararg profiles: String) {
        environment!!.setDefaultProfiles(*profiles)
    }

    @JvmStatic
    fun acceptsProfiles(profiles: Profiles): Boolean {
        return environment!!.acceptsProfiles(profiles)
    }

    @JvmStatic
    val replaceMapPropertySource: MutableMap<String, Any>
        get() {
            val replaceEnvironment = "replaceEnvironment"
            val propertySources: MutablePropertySources = propertySources
            var target: MapPropertySource? = null

            if (propertySources.contains(replaceEnvironment)) {
                val source = propertySources.get(replaceEnvironment)
                if (source is MapPropertySource) {
                    target = source
                }
            } else {
                target = MapPropertySource(replaceEnvironment, HashMap<String, Any>())
                propertySources.addFirst(target)
            }

            return Optional.ofNullable<MapPropertySource>(target).map<MutableMap<String, Any>>(Function { obj -> obj.getSource() }).orElse(null)
        }

    /**
     * 检测指定环境是否已激活
     * @return true 表示为为指定环境
     */
    @JvmStatic
    fun isActiveProfiles(vararg profiles: String): Boolean {
        val activeProfiles: Array<String> = environment!!.activeProfiles

        for (profile in profiles) {
            // 未激活指定环境
            if (!containsIgnoreCase(activeProfiles, profile)) {
                return false
            }
        }

        return true
    }

}
