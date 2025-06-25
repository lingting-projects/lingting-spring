package live.lingting.spring.mybatis

import live.lingting.framework.mybatis.typehandler.DataSizeTypeHandler
import live.lingting.framework.mybatis.typehandler.MoneyTypeHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2025/6/25 18:16
 */
@AutoConfiguration
open class MybatisTypeHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun moneyTypeHandler() = MoneyTypeHandler()

    @Bean
    @ConditionalOnMissingBean
    open fun dataSizeTypeHandler() = DataSizeTypeHandler()

}
