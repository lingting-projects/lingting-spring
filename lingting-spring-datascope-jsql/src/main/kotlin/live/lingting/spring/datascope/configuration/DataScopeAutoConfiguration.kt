package live.lingting.spring.datascope.configuration

import live.lingting.framework.datascope.JsqlDataScope
import live.lingting.framework.datascope.handler.DataPermissionHandler
import live.lingting.framework.datascope.handler.DefaultDataPermissionHandler
import live.lingting.framework.datascope.parser.DataScopeParser
import live.lingting.framework.datascope.parser.DefaultDataScopeParser
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
    open fun dataPermissionHandler(scopes: MutableList<JsqlDataScope>): DataPermissionHandler {
        return DefaultDataPermissionHandler(scopes)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun dataScopeParser(): DataScopeParser {
        return DefaultDataScopeParser()
    }
}
