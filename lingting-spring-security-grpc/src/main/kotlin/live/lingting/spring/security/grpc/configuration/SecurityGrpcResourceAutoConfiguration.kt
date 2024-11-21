package live.lingting.spring.security.grpc.configuration;

import io.grpc.ManagedChannel;
import live.lingting.framework.convert.SecurityGrpcConvert;
import live.lingting.framework.grpc.GrpcClientProvide;
import live.lingting.framework.interceptor.SecurityGrpcRemoteResourceClientInterceptor;
import live.lingting.framework.interceptor.SecurityGrpcResourceServerInterceptor;
import live.lingting.framework.properties.SecurityGrpcProperties;
import live.lingting.framework.resource.SecurityTokenGrpcRemoteResolver;
import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.framework.security.properties.SecurityProperties;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.spring.grpc.configuration.GrpcAutoConfiguration;
import live.lingting.spring.security.conditional.ConditionalOnResource;
import live.lingting.spring.security.conditional.ConditionalOnUsingRemoteAuthorization;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnResource
@AutoConfiguration(after = GrpcAutoConfiguration.class, beforeName = "SecurityWebResourceAutoConfiguration")
public class SecurityGrpcResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcResourceServerInterceptor securityGrpcResourceServerInterceptor(
			SecurityGrpcProperties properties, SecurityResourceService service, SecurityAuthorize authorize) {
		return new SecurityGrpcResourceServerInterceptor(properties.authorizationKey(), service, authorize);
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
	public SecurityTokenGrpcRemoteResolver securityTokenGrpcRemoteResolver(SecurityProperties properties,
			SecurityGrpcRemoteResourceClientInterceptor interceptor, GrpcClientProvide provide,
			SecurityGrpcConvert convert) {
		SecurityProperties.Authorization authorization = properties.getAuthorization();
		ManagedChannel channel = provide.builder(authorization.getRemoteHost())
			.provide()
			.interceptor(interceptor)
			.build();
		return new SecurityTokenGrpcRemoteResolver(channel, convert);
	}

}
