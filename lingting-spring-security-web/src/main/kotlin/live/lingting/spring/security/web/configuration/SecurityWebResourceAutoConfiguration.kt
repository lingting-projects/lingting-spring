package live.lingting.spring.security.web.configuration

import jakarta.servlet.DispatcherType
import java.util.EnumSet
import live.lingting.framework.http.api.ApiClient
import live.lingting.framework.security.authorize.SecurityAuthorize
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.properties.SecurityProperties
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.spring.security.conditional.ConditionalOnResource
import live.lingting.spring.security.conditional.ConditionalOnUsingRemoteAuthorization
import live.lingting.spring.security.web.properties.SecurityWebProperties
import live.lingting.spring.security.web.resource.SecurityTokenWebRemoteResolver
import live.lingting.spring.security.web.resource.SecurityWebResourceFilter
import live.lingting.spring.security.web.resource.SecurityWebResourceInterceptor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author lingting 2024-03-21 19:41
 */
@AutoConfiguration
@ConditionalOnResource
open class SecurityWebResourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(
        value = [SecurityTokenWebRemoteResolver::class],
        name = ["securityTokenGrpcRemoteResolver"]
    )
    @ConditionalOnUsingRemoteAuthorization
    open fun securityTokenWebRemoteResolver(
        properties: SecurityProperties,
        convert: SecurityConvert, webProperties: SecurityWebProperties
    ): SecurityTokenWebRemoteResolver {
        val authorization = properties.authorization
        val remoteHost = authorization.remoteHost
        return SecurityTokenWebRemoteResolver(remoteHost!!, ApiClient.CLIENT, convert, webProperties)
    }

    @Bean
    @ConditionalOnMissingFilterBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    open fun securityWebResourceFilter(
        properties: SecurityProperties,
        webProperties: SecurityWebProperties, service: SecurityResourceService
    ): FilterRegistrationBean<SecurityWebResourceFilter> {
        val filter = SecurityWebResourceFilter(webProperties, service)
        val bean = FilterRegistrationBean<SecurityWebResourceFilter>(filter)
        bean.setDispatcherTypes(EnumSet.allOf<DispatcherType>(DispatcherType::class.java))
        bean.order = properties.order
        return bean
    }

    @Bean
    @ConditionalOnMissingBean
    open fun securityWebResourceInterceptor(
        properties: SecurityWebProperties,
        authorize: SecurityAuthorize
    ): SecurityWebResourceInterceptor {
        return SecurityWebResourceInterceptor(properties, authorize)
    }

    @Bean
    @ConditionalOnBean(SecurityWebResourceInterceptor::class)
    open fun securityWebResourceWebMvcConfigurer(interceptor: SecurityWebResourceInterceptor): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addInterceptors(registry: InterceptorRegistry) {
                registry.addInterceptor(interceptor).addPathPatterns("/**")
            }
        }
    }
}
