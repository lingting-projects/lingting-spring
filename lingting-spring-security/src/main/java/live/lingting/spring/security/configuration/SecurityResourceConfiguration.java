package live.lingting.spring.security.configuration;

import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.framework.security.resolver.SecurityTokenDefaultResolver;
import live.lingting.framework.security.resolver.SecurityTokenResolver;
import live.lingting.framework.security.resource.SecurityDefaultResourceServiceImpl;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.framework.security.store.SecurityMemoryStore;
import live.lingting.framework.security.store.SecurityStore;
import live.lingting.spring.security.conditional.ConditionalOnUsingLocalAuthorization;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lingting 2023-03-29 21:09
 */
public class SecurityResourceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityAuthorize securityAuthorize(SecurityProperties properties) {
		return new SecurityAuthorize(properties.getOrder());
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityResourceService securityStoreResourceService(List<SecurityTokenResolver> resolvers) {
		return new SecurityDefaultResourceServiceImpl(resolvers);
	}

	@Bean
	@ConditionalOnUsingLocalAuthorization
	public SecurityTokenDefaultResolver securityTokenDefaultResolver(ObjectProvider<SecurityStore> provider) {
		AtomicReference<SecurityStore> store = new AtomicReference<>(new SecurityMemoryStore());
		provider.ifAvailable(store::set);
		return new SecurityTokenDefaultResolver(store.get());
	}

}
