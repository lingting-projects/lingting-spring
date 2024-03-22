package live.lingting.spring.security.configuration;

import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.framework.security.resource.SecurityDefaultResourceServiceImpl;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.framework.security.store.SecurityStore;
import live.lingting.spring.security.properties.SecuritySpringProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:09
 */
public class SecurityResourceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityAuthorize securityAuthorize(SecurityProperties properties) {
		return new SecurityAuthorize(properties.getOrder());
	}

	/**
	 * 使用本地解析token
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecuritySpringProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "false", matchIfMissing = true)
	public SecurityResourceService securityStoreResourceService(SecurityStore store) {
		return new SecurityDefaultResourceServiceImpl(store);
	}

}
