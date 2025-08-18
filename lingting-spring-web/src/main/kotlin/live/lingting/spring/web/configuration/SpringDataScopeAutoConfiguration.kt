package live.lingting.spring.web.configuration

import jakarta.servlet.DispatcherType
import live.lingting.framework.datascope.rule.DataScopeRuleHolder
import live.lingting.spring.web.filter.WebDataScopeFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import java.util.EnumSet

/**
 * @author lingting 2025/8/18 16:55
 */
@AutoConfiguration
@ConditionalOnClass(DataScopeRuleHolder::class)
open class SpringDataScopeAutoConfiguration {

    @Bean
    @ConditionalOnMissingFilterBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    open fun webDataScopeFilter(): FilterRegistrationBean<WebDataScopeFilter> {
        val filter = WebDataScopeFilter()
        val bean = FilterRegistrationBean(filter)
        bean.setDispatcherTypes(EnumSet.allOf(DispatcherType::class.java))
        bean.order = Int.MIN_VALUE
        return bean
    }

}
