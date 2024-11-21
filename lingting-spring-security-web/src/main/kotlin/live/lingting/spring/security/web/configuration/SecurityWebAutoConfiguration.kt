package live.lingting.spring.security.web.configuration

import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.spring.security.web.exception.SecurityWebExceptionHandler
import live.lingting.spring.security.web.properties.SecurityWebProperties
import live.lingting.spring.web.exception.DefaultExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-03-20 19:52
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityWebProperties::class)
open class SecurityWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DefaultExceptionHandler::class)
    open fun securityWebExceptionHandler(): SecurityWebExceptionHandler {
        return SecurityWebExceptionHandler()
    }

    @Bean
    @ConditionalOnMissingBean(SecurityConvert::class)
    open fun securityConvert(): SecurityConvert {
        return object : SecurityConvert {
        }
    }
}
