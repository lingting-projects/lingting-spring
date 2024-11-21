package live.lingting.spring.aws

import live.lingting.framework.aws.AwsS3Bucket
import live.lingting.framework.aws.s3.AwsS3Properties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(AwsSpringProperties::class)
class AwsAutoConfiguration {
    @Bean
    fun awsS3Properties(properties: AwsSpringProperties): AwsS3Properties {
        return properties.getS3()
    }

    @Bean
    @ConditionalOnProperty(prefix = AwsSpringProperties.PREFIX + ".s3", name = ["ak"])
    fun awsS3Bucket(properties: AwsS3Properties): AwsS3Bucket {
        return AwsS3Bucket(properties)
    }
}
