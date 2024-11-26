package live.lingting.spring.elasticsearch.configuration

import live.lingting.framework.datascope.DataScope
import live.lingting.framework.elasticsearch.datascope.DataScopeInterceptor
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024/11/26 15:27
 */
@AutoConfiguration
@ConditionalOnClass(DataScope::class, ElasticsearchDataScope::class)
open class ElasticsearchDataScopeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun elasticsearchDataScopeInterceptor(scopes: List<ElasticsearchDataScope>): DataScopeInterceptor {
        return DataScopeInterceptor(scopes)
    }

}
