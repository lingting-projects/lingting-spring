package live.lingting.spring.web.configuration

import live.lingting.spring.web.converter.AbstractConverter
import live.lingting.spring.web.converter.Converter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.ConverterRegistry

/**
 * @author lingting 2024-03-20 15:58
 */
@AutoConfiguration
@ComponentScan("live.lingting.spring.web.converter")
@ConditionalOnBean(ConverterRegistry::class, ConversionService::class)
class SpringWebConverterAutoConfiguration(
    service: ConversionService, registry: ConverterRegistry,
    converters: MutableList<Converter<*>>
) {
    init {
        for (converter in converters) {
            registry.addConverter(converter)
            if (converter is AbstractConverter<*>) {
                converter.setService(service)
            }
        }
    }
}
