package live.lingting.spring.web.configuration

import live.lingting.spring.web.converter.EnumConverter
import live.lingting.spring.web.converter.MoneyConverter
import live.lingting.spring.web.converter.StringToArrayConverter
import live.lingting.spring.web.converter.StringToCollectionConverter
import live.lingting.spring.web.converter.StringToLocalDateConverter
import live.lingting.spring.web.converter.StringToLocalDateTimeConverter
import live.lingting.spring.web.converter.StringToLocalTimeConverter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-03-20 15:58
 */
@AutoConfiguration
open class SpringWebConverterAutoConfiguration() {

    @Bean
    @ConditionalOnMissingBean
    open fun enumConverter(): EnumConverter {
        return EnumConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun moneyConverter(): MoneyConverter {
        return MoneyConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun stringToArrayConverter(): StringToArrayConverter {
        return StringToArrayConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun stringToCollectionConverter(): StringToCollectionConverter {
        return StringToCollectionConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun stringToLocalDateConverter(): StringToLocalDateConverter {
        return StringToLocalDateConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun stringToLocalDateTimeConverter(): StringToLocalDateTimeConverter {
        return StringToLocalDateTimeConverter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun stringToLocalTimeConverter(): StringToLocalTimeConverter {
        return StringToLocalTimeConverter()
    }

}
