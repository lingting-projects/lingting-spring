package live.lingting.spring.ali

import live.lingting.framework.ali.AliOssBucket
import live.lingting.framework.ali.AliSts
import live.lingting.framework.ali.properties.AliOssProperties
import live.lingting.framework.ali.properties.AliStsProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(AliSpringProperties::class)
open class AliAutoConfiguration {
    @Bean
    open fun aliStsProperties(properties: AliSpringProperties): AliStsProperties {
        return properties.sts
    }

    @Bean
    open fun aliOssProperties(properties: AliSpringProperties): AliOssProperties {
        return properties.oss
    }

    @Bean
    @ConditionalOnProperty(prefix = AliSpringProperties.PREFIX + ".sts", name = ["ak"])
    open fun aliSts(properties: AliStsProperties): AliSts {
        return AliSts(properties)
    }

    @Bean
    @ConditionalOnProperty(prefix = AliSpringProperties.PREFIX + ".oss", name = ["ak"])
    open fun aliOssBucket(properties: AliOssProperties): AliOssBucket {
        return AliOssBucket(properties)
    }
}
