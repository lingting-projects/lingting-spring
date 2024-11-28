package live.lingting.spring.security.grpc.configuration

import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.grpc.convert.SecurityGrpcConvert
import live.lingting.framework.security.grpc.exception.SecurityGrpcExceptionInstance
import live.lingting.framework.security.grpc.properties.SecurityGrpcProperties
import live.lingting.spring.security.grpc.properties.SecurityGrpcSpringProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-05 19:12
 */
@AutoConfiguration(beforeName = ["SecurityWebAutoConfiguration"])
@EnableConfigurationProperties(SecurityGrpcSpringProperties::class)
open class SecurityGrpcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    open fun securityGrpcProperties(springProperties: SecurityGrpcSpringProperties): SecurityGrpcProperties {
        return springProperties.properties()
    }

    @Bean
    @ConditionalOnMissingBean(SecurityConvert::class)
    open fun securityGrpcConvert(): SecurityGrpcConvert {
        return SecurityGrpcConvert()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun securityGrpcExceptionInstance(): SecurityGrpcExceptionInstance {
        return SecurityGrpcExceptionInstance()
    }
}
