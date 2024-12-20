package live.lingting.spring.huawei

import live.lingting.framework.huawei.properties.HuaweiIamProperties
import live.lingting.framework.huawei.properties.HuaweiObsProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(HuaweiSpringProperties.PREFIX)
class HuaweiSpringProperties {

    companion object {
        const val PREFIX: String = "lingting.huawei"
    }

    @NestedConfigurationProperty
    var iam: HuaweiIamProperties = HuaweiIamProperties()

    @NestedConfigurationProperty
    var obs: HuaweiObsProperties = HuaweiObsProperties()

}
