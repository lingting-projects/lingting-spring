package live.lingting.spring.security.web.configuration

import live.lingting.framework.security.SecurityEndpointService
import live.lingting.spring.security.conditional.ConditionalOnAuthorization
import live.lingting.spring.security.web.endpoint.SecurityWebEndpoint
import live.lingting.spring.security.web.endpoint.SecurityWebEndpointHandlerMapping
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.format.WebConversionService
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.HandlerInterceptor

/**
 * @author lingting 2024-03-20 19:59
 */
@AutoConfiguration
@ConditionalOnAuthorization
open class SecurityWebAuthorizationAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    open fun securityWebEndpoint(service: SecurityEndpointService): SecurityWebEndpoint {
        return SecurityWebEndpoint(service)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun securityWebEndpointHandlerMapping(
        @Qualifier("mvcConversionService") conversionService: WebConversionService,
        interceptors: MutableList<HandlerInterceptor>
    ): SecurityWebEndpointHandlerMapping {
        return SecurityWebEndpointHandlerMapping(conversionService, interceptors)
    }
}
