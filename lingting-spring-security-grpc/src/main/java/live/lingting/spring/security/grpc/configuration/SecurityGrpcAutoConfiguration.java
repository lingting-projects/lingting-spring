package live.lingting.spring.security.grpc.configuration;

import live.lingting.framework.convert.SecurityGrpcConvert;
import live.lingting.framework.exception.SecurityGrpcExceptionInstance;
import live.lingting.framework.properties.SecurityGrpcProperties;
import live.lingting.framework.security.convert.SecurityConvert;
import live.lingting.spring.security.grpc.properties.SecurityGrpcSpringProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-02-05 19:12
 */
@AutoConfiguration(beforeName = "SecurityWebAutoConfiguration")
@EnableConfigurationProperties(SecurityGrpcSpringProperties.class)
public class SecurityGrpcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcProperties securityGrpcProperties(SecurityGrpcSpringProperties springProperties) {
		return springProperties.properties();
	}

	@Bean
	@ConditionalOnMissingBean(SecurityConvert.class)
	public SecurityGrpcConvert securityGrpcConvert() {
		return new SecurityGrpcConvert();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcExceptionInstance securityGrpcExceptionInstance() {
		return new SecurityGrpcExceptionInstance();
	}

}
