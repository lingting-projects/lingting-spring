package live.lingting.spring.web.configuration

import jakarta.servlet.DispatcherType
import java.util.EnumSet
import live.lingting.spring.web.filter.WebScopeFilter
import live.lingting.spring.web.properties.SpringWebProperties
import live.lingting.spring.web.request.ServletRequestConsumer
import live.lingting.spring.web.resolve.ApiPaginationParamsResolve
import org.hibernate.validator.BaseHibernateValidatorConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author lingting 2024-03-20 14:59
 */
@AutoConfigureBefore(ValidationAutoConfiguration::class)
@EnableConfigurationProperties(SpringWebProperties::class)
@AutoConfiguration(before = [ValidationAutoConfiguration::class])
open class SpringWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingFilterBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    open fun webScopeFilter(properties: SpringWebProperties, consumers: List<ServletRequestConsumer>): FilterRegistrationBean<WebScopeFilter> {
        val filter = WebScopeFilter(properties, consumers)
        val bean = FilterRegistrationBean<WebScopeFilter>(filter)
        bean.setDispatcherTypes(EnumSet.allOf<DispatcherType>(DispatcherType::class.java))
        bean.order = properties.scopeFilterOrder
        return bean
    }

    @Bean
    @ConditionalOnMissingBean
    open fun apiPaginationParamsResolve(properties: SpringWebProperties): ApiPaginationParamsResolve {
        return ApiPaginationParamsResolve(properties.pagination)
    }

    @Bean
    @ConditionalOnBean(ApiPaginationParamsResolve::class)
    open fun webMvcConfigurer(resolve: ApiPaginationParamsResolve): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
                resolvers.add(resolve)
            }
        }
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    open fun lingtingSpringValidationConfigurationCustomizer(): ValidationConfigurationCustomizer {
        return ValidationConfigurationCustomizer { configuration ->
            configuration.addProperty(BaseHibernateValidatorConfiguration.FAIL_FAST, "true")
        }
    }
}
