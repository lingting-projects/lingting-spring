package live.lingting.spring.security.web.configuration;

import jakarta.servlet.DispatcherType;
import live.lingting.framework.okhttp.OkHttp3Builder;
import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.framework.security.convert.SecurityConvert;
import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.spring.security.conditional.ConditionalOnResource;
import live.lingting.spring.security.conditional.ConditionalOnUsingRemoteAuthorization;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import live.lingting.spring.security.web.resource.SecurityWebDefaultRemoteResourceServiceImpl;
import live.lingting.spring.security.web.resource.SecurityWebResourceFilter;
import live.lingting.spring.security.web.resource.SecurityWebResourceInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.EnumSet;

/**
 * @author lingting 2024-03-21 19:41
 */
@AutoConfiguration
@ConditionalOnResource
public class SecurityWebResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnUsingRemoteAuthorization
	public SecurityResourceService securityResourceService(SecurityProperties properties, SecurityConvert convert,
			SecurityWebProperties webProperties) {
		OkHttp3Builder builder = OkHttp3Builder.builder()
			.disableSsl()
			.timeout(Duration.ofSeconds(5), Duration.ofSeconds(10));

		SecurityProperties.Authorization authorization = properties.getAuthorization();
		return new SecurityWebDefaultRemoteResourceServiceImpl(authorization.getRemoteHost(), builder.build(), convert,
				webProperties);
	}

	@Bean
	@ConditionalOnMissingFilterBean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public FilterRegistrationBean<SecurityWebResourceFilter> securityWebResourceFilter(SecurityProperties properties,
			SecurityWebProperties webProperties, SecurityResourceService service) {
		SecurityWebResourceFilter filter = new SecurityWebResourceFilter(webProperties, service);
		FilterRegistrationBean<SecurityWebResourceFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		bean.setOrder(properties.getOrder());
		return bean;
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebResourceInterceptor securityWebResourceInterceptor(SecurityWebProperties properties,
			SecurityAuthorize authorize) {
		return new SecurityWebResourceInterceptor(properties, authorize);
	}

	@Bean
	@ConditionalOnBean(SecurityWebResourceInterceptor.class)
	public WebMvcConfigurer securityWebResourceWebMvcConfigurer(SecurityWebResourceInterceptor interceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor).addPathPatterns("/**");
			}
		};
	}

}
