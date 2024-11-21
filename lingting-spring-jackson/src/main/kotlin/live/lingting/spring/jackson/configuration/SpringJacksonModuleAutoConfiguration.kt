package live.lingting.spring.jackson.configuration

import live.lingting.framework.jackson.module.BooleanModule
import live.lingting.framework.jackson.module.EnumModule
import live.lingting.framework.jackson.module.JavaTimeModule
import live.lingting.framework.jackson.module.MoneyModule
import live.lingting.framework.jackson.module.RModule
import live.lingting.framework.jackson.provider.NullSerializerProvider
import live.lingting.framework.jackson.sensitive.SensitiveModule
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-02 13:52
 */
@AutoConfiguration
class SpringJacksonModuleAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun javaTimeModule(): JavaTimeModule {
        return JavaTimeModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun enumModule(): EnumModule {
        return EnumModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun booleanModule(): BooleanModule {
        return BooleanModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun rModule(): RModule {
        return RModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun sensitiveModule(): SensitiveModule {
        return SensitiveModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun moneyModule(): MoneyModule {
        return MoneyModule()
    }

    @Bean
    @ConditionalOnMissingBean
    fun nullSerializerProvider(): NullSerializerProvider {
        return NullSerializerProvider()
    }
}
