package live.lingting.spring.web.configuration

import jakarta.servlet.DispatcherType
import jakarta.validation.Configuration
import java.util.EnumSet
import live.lingting.spring.web.filter.WebScopeFilter
import live.lingting.spring.web.properties.SpringWebProperties
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
class SpringWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingFilterBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    fun webScopeFilter(properties: SpringWebProperties): FilterRegistrationBean<WebScopeFilter> {
        val filter = WebScopeFilter(properties)
        val bean = FilterRegistrationBean<WebScopeFilter>(filter)
        bean.setDispatcherTypes(EnumSet.allOf<DispatcherType>(DispatcherType::class.java))
        bean.setOrder(properties.getScopeFilterOrder())
        return bean
    }

    @Bean
    @ConditionalOnMissingBean
    fun apiPaginationParamsResolve(properties: SpringWebProperties): ApiPaginationParamsResolve {
        return ApiPaginationParamsResolve(properties.getPagination())
    }

    @Bean
    @ConditionalOnBean(ApiPaginationParamsResolve::class)
    fun webMvcConfigurer(resolve: ApiPaginationParamsResolve): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
                resolvers!!.add(resolve)
            }
        }
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun lingtingSpringValidationConfigurationCustomizer(): ValidationConfigurationCustomizer {
        return ValidationConfigurationCustomizer { configuration: Configuration<*> -> configuration!!.addProperty(BaseHibernateValidatorConfiguration.FAIL_FAST, "true") }
    }
}
