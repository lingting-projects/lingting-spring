package live.lingting.spring.datascope.configuration

import live.lingting.spring.datascope.rule.DataScopeRuleInterceptor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-02 16:40
 */
@AutoConfiguration
open class DataScopeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun dataScopeRuleInterceptor(): DataScopeRuleInterceptor {
        return DataScopeRuleInterceptor()
    }
}
