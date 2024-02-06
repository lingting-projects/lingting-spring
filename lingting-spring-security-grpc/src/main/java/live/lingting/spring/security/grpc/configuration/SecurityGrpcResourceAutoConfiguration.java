package live.lingting.spring.security.grpc.configuration;

import live.lingting.framework.convert.SecurityGrpcConvert;
import live.lingting.framework.exception.SecurityGrpcExceptionHandler;
import live.lingting.framework.grpc.GrpcClientProvide;
import live.lingting.framework.interceptor.SecurityGrpcRemoteResourceClientInterceptor;
import live.lingting.framework.interceptor.SecurityGrpcResourceServerInterceptor;
import live.lingting.framework.properties.SecurityGrpcProperties;
import live.lingting.framework.resource.SecurityGrpcDefaultRemoteResourceServiceImpl;
import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.spring.grpc.configuration.GrpcAutoConfiguration;
import live.lingting.spring.security.conditional.ConditionalOnResource;
import live.lingting.spring.security.conditional.ConditionalOnUsingRemoteAuthorization;
import live.lingting.spring.security.grpc.properties.SecurityGrpcSpringProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnResource
@AutoConfiguration(after = GrpcAutoConfiguration.class)
public class SecurityGrpcResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcResourceServerInterceptor securityGrpcResourceServerInterceptor(
		SecurityGrpcSpringProperties properties, SecurityResourceService service, SecurityAuthorize authorize,
		SecurityGrpcExceptionHandler exceptionHandler) {
		return new SecurityGrpcResourceServerInterceptor(properties.properties().authorizationKey(), service, authorize,
			exceptionHandler);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnUsingRemoteAuthorization
	public SecurityGrpcRemoteResourceClientInterceptor securityGrpcRemoteResourceClientInterceptor(
		SecurityGrpcProperties properties) {
		return new SecurityGrpcRemoteResourceClientInterceptor(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnUsingRemoteAuthorization
	@ConditionalOnBean(GrpcClientProvide.class)
	public SecurityResourceService serviceGrpcRemoteResourceService(SecurityProperties properties,
																	SecurityGrpcRemoteResourceClientInterceptor interceptor, GrpcClientProvide provide,
																	SecurityGrpcConvert convert) {
		return new SecurityGrpcDefaultRemoteResourceServiceImpl(properties, interceptor, provide, convert);
	}

}
