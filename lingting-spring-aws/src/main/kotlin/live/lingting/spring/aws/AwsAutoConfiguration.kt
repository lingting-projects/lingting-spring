package live.lingting.spring.aws

import live.lingting.framework.aws.AwsS3Bucket
import live.lingting.framework.aws.AwsSts
import live.lingting.framework.aws.properties.AwsS3Properties
import live.lingting.framework.aws.properties.AwsStsProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(AwsSpringProperties::class)
open class AwsAutoConfiguration {

    @Bean
    open fun awsStsProperties(properties: AwsSpringProperties): AwsStsProperties {
        return properties.sts
    }

    @Bean
    open fun awsS3Properties(properties: AwsSpringProperties): AwsS3Properties {
        return properties.s3
    }

    @Bean
    @ConditionalOnProperty(prefix = AwsSpringProperties.PREFIX + ".sts", name = ["ak"])
    open fun awsSts(properties: AwsStsProperties): AwsSts {
        return AwsSts(properties)
    }

    @Bean
    @ConditionalOnProperty(prefix = AwsSpringProperties.PREFIX + ".s3", name = ["ak"])
    open fun awsS3Bucket(properties: AwsS3Properties): AwsS3Bucket {
        return AwsS3Bucket(properties)
    }

}
