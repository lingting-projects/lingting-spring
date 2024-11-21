package live.lingting.spring.web.configuration;

import live.lingting.spring.web.converter.AbstractConverter;
import live.lingting.spring.web.converter.Converter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.util.List;

/**
 * @author lingting 2024-03-20 15:58
 */
@AutoConfiguration
@ComponentScan("live.lingting.spring.web.converter")
@ConditionalOnBean({ ConverterRegistry.class, ConversionService.class })
public class SpringWebConverterAutoConfiguration {

	public SpringWebConverterAutoConfiguration(ConversionService service, ConverterRegistry registry,
			List<Converter<?>> converters) {
		for (Converter<?> converter : converters) {
			registry.addConverter(converter);
			if (converter instanceof AbstractConverter<?> abstractConverter) {
				abstractConverter.setService(service);
			}
		}
	}

}
