package live.lingting.spring.huawei

import live.lingting.framework.huawei.HuaweiIam
import live.lingting.framework.huawei.HuaweiObsBucket
import live.lingting.framework.huawei.properties.HuaweiIamProperties
import live.lingting.framework.huawei.properties.HuaweiObsProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(HuaweiSpringProperties::class)
open class HuaweiAutoConfiguration {
    @Bean
    open fun huaweiIamProperties(properties: HuaweiSpringProperties): HuaweiIamProperties {
        return properties.iam
    }

    @Bean
    open fun huaweiObsProperties(properties: HuaweiSpringProperties): HuaweiObsProperties {
        return properties.obs
    }

    @Bean
    @ConditionalOnProperty(prefix = HuaweiSpringProperties.PREFIX + ".iam", name = ["username"])
    open fun huaweiIam(properties: HuaweiIamProperties): HuaweiIam {
        return HuaweiIam(properties)
    }

    @Bean
    @ConditionalOnProperty(prefix = HuaweiSpringProperties.PREFIX + ".obs", name = ["ak"])
    open fun huaweiObsBucket(properties: HuaweiObsProperties): HuaweiObsBucket {
        return HuaweiObsBucket(properties)
    }
}
