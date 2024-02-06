package live.lingting.spring.security.grpc.configuration;

import live.lingting.framework.convert.SecurityGrpcConvert;
import live.lingting.framework.exception.SecurityGrpcExceptionHandler;
import live.lingting.framework.properties.SecurityGrpcProperties;
import live.lingting.spring.security.grpc.properties.SecurityGrpcSpringProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-02-05 19:12
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityGrpcSpringProperties.class)
public class SecurityGrpcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcProperties securityGrpcProperties(SecurityGrpcSpringProperties springProperties) {
		return springProperties.properties();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcExceptionHandler securityGrpcExceptionHandler() {
		return new SecurityGrpcExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcConvert securityGrpcConvert() {
		return new SecurityGrpcConvert();
	}

}
