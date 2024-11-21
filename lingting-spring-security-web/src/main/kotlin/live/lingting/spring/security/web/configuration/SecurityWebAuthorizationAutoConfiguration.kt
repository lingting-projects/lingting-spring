package live.lingting.spring.security.web.configuration;

import live.lingting.framework.security.authorize.SecurityAuthorizationService;
import live.lingting.framework.security.convert.SecurityConvert;
import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.security.store.SecurityStore;
import live.lingting.spring.security.conditional.ConditionalOnAuthorization;
import live.lingting.spring.security.web.endpoint.SecurityWebEndpoint;
import live.lingting.spring.security.web.endpoint.SecurityWebEndpointHandlerMapping;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @author lingting 2024-03-20 19:59
 */
@AutoConfiguration
@ConditionalOnAuthorization
public class SecurityWebAuthorizationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebEndpoint securityWebEndpoint(SecurityAuthorizationService service, SecurityStore store,
			SecurityPassword password, SecurityConvert convert) {
		return new SecurityWebEndpoint(service, store, password, convert);
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebEndpointHandlerMapping securityWebEndpointHandlerMapping(
			@Qualifier("mvcConversionService") WebConversionService conversionService,
			List<HandlerInterceptor> interceptors) {
		return new SecurityWebEndpointHandlerMapping(conversionService, interceptors);
	}

}
