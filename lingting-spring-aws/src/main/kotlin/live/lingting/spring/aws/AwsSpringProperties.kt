package live.lingting.spring.aws

import live.lingting.framework.aws.properties.AwsS3Properties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(AwsSpringProperties.PREFIX)
class AwsSpringProperties {

    companion object {
        const val PREFIX: String = "lingting.aws"
    }

    @NestedConfigurationProperty
    var s3: AwsS3Properties = AwsS3Properties()

}
