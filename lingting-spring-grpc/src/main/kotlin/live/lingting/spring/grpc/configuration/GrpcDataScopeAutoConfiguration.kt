package live.lingting.spring.grpc.configuration

import live.lingting.framework.datascope.rule.DataScopeRuleHolder
import live.lingting.framework.grpc.interceptor.GrpcDataScopeInterceptor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2025/8/18 16:55
 */
@AutoConfiguration
@ConditionalOnClass(DataScopeRuleHolder::class)
open class GrpcDataScopeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun grpcDataScopeInterceptor() = GrpcDataScopeInterceptor()

}
