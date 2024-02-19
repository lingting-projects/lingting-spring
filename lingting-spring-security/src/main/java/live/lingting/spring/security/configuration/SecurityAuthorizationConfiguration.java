package live.lingting.spring.security.configuration;

import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.security.store.SecurityMemoryStore;
import live.lingting.framework.security.store.SecurityStore;
import live.lingting.spring.security.password.SecurityDefaultPassword;
import live.lingting.spring.security.properties.SecuritySpringProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:56
 */
public class SecurityAuthorizationConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityStore securityStore() {
		return new SecurityMemoryStore();
	}

	/**
	 * 使用 password-secret-key 可以匹配 - 和驼峰
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecuritySpringProperties.PREFIX + ".authorization", name = "password-secret-key")
	public SecurityPassword securityPassword(SecuritySpringProperties properties) {
		SecuritySpringProperties.Authorization authorization = properties.getAuthorization();
		return new SecurityDefaultPassword(authorization.getPasswordSecretKey());
	}

}
