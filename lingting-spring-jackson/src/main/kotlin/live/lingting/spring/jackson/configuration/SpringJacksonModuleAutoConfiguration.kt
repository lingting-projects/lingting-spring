package live.lingting.spring.jackson.configuration;

import live.lingting.framework.jackson.module.BooleanModule;
import live.lingting.framework.jackson.module.EnumModule;
import live.lingting.framework.jackson.module.JavaTimeModule;
import live.lingting.framework.jackson.module.MoneyModule;
import live.lingting.framework.jackson.module.RModule;
import live.lingting.framework.jackson.provider.NullSerializerProvider;
import live.lingting.framework.jackson.sensitive.SensitiveModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-02-02 13:52
 */
@AutoConfiguration
public class SpringJacksonModuleAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JavaTimeModule javaTimeModule() {
		return new JavaTimeModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public EnumModule enumModule() {
		return new EnumModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public BooleanModule booleanModule() {
		return new BooleanModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public RModule rModule() {
		return new RModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public SensitiveModule sensitiveModule() {
		return new SensitiveModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public MoneyModule moneyModule() {
		return new MoneyModule();
	}

	@Bean
	@ConditionalOnMissingBean
	public NullSerializerProvider nullSerializerProvider() {
		return new NullSerializerProvider();
	}

}
