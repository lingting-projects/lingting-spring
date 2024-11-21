package live.lingting.spring.security.web.configuration

import live.lingting.framework.security.authorize.SecurityAuthorizationService
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.framework.security.store.SecurityStore
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
class SecurityWebAuthorizationAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun securityWebEndpoint(
        service: SecurityAuthorizationService, store: SecurityStore,
        password: SecurityPassword, convert: SecurityConvert
    ): SecurityWebEndpoint {
        return SecurityWebEndpoint(service, store, password, convert)
    }

    @Bean
    @ConditionalOnMissingBean
    fun securityWebEndpointHandlerMapping(
        @Qualifier("mvcConversionService") conversionService: WebConversionService,
        interceptors: MutableList<HandlerInterceptor>
    ): SecurityWebEndpointHandlerMapping {
        return SecurityWebEndpointHandlerMapping(conversionService, interceptors)
    }
}
