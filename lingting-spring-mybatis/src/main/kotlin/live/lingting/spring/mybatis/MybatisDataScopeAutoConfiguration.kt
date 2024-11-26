package live.lingting.spring.mybatis

import live.lingting.framework.datascope.DataScope
import live.lingting.framework.mybatis.datascope.DataScopeInterceptor
import live.lingting.framework.mybatis.datascope.DefaultJSqlDataScopeParserFactory
import live.lingting.framework.mybatis.datascope.JSqlDataScope
import live.lingting.framework.mybatis.datascope.JSqlDataScopeParserFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024/11/25 16:20
 */
@AutoConfiguration
@ConditionalOnClass(DataScope::class, JSqlDataScope::class)
open class MybatisDataScopeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun jSqlDataScopeParserFactory(): JSqlDataScopeParserFactory {
        return DefaultJSqlDataScopeParserFactory()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun mybatisDataScopeInterceptor(factory: JSqlDataScopeParserFactory, scopes: List<JSqlDataScope>): DataScopeInterceptor {
        return DataScopeInterceptor(factory, scopes)
    }

}
