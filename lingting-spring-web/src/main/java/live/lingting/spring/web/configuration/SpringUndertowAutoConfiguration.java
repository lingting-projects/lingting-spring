package live.lingting.spring.web.configuration;

import io.undertow.Undertow;
import jakarta.servlet.ServletContext;
import live.lingting.spring.web.properties.SpringWebProperties;
import live.lingting.spring.web.undertow.UndertowTimer;
import live.lingting.spring.web.undertow.UndertowWebCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-03-20 16:04
 */
@AutoConfiguration
@ConditionalOnClass(Undertow.class)
@ConditionalOnBean(UndertowServletWebServerFactory.class)
public class SpringUndertowAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UndertowWebCustomizer undertowWebCustomizer(SpringWebProperties properties) {
		return new UndertowWebCustomizer(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ServletContext.class)
	public UndertowTimer undertowTimer(ServletContext context) {
		return new UndertowTimer(context);
	}

}
