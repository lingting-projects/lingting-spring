package live.lingting.spring.ali

import live.lingting.framework.ali.properties.AliOssProperties
import live.lingting.framework.ali.properties.AliStsProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(AliSpringProperties.PREFIX)
class AliSpringProperties {
    @NestedConfigurationProperty
    var sts: AliStsProperties = AliStsProperties()

    @NestedConfigurationProperty
    var oss: AliOssProperties = AliOssProperties()

    companion object {
        const val PREFIX: String = "lingting.ali"
    }
}
