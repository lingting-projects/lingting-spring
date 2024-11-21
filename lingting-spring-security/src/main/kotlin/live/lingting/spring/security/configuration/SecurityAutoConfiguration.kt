package live.lingting.spring.security.configuration;

import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.spring.security.properties.SecuritySpringProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 20:51
 */
@AutoConfiguration
@EnableConfigurationProperties(SecuritySpringProperties.class)
public class SecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityProperties securityProperties(SecuritySpringProperties springProperties) {
		return springProperties.properties();
	}

}
