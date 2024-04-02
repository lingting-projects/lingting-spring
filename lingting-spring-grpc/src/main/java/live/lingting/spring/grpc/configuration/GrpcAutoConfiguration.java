package live.lingting.spring.grpc.configuration;

import io.grpc.ClientInterceptor;
import live.lingting.framework.grpc.GrpcClientProvide;
import live.lingting.framework.grpc.GrpcServer;
import live.lingting.framework.grpc.GrpcServerBuilder;
import live.lingting.framework.grpc.interceptor.GrpcClientTraceIdInterceptor;
import live.lingting.framework.grpc.properties.GrpcClientProperties;
import live.lingting.framework.grpc.properties.GrpcServerProperties;
import live.lingting.spring.grpc.mapstruct.SpringGrpcMapstruct;
import live.lingting.spring.grpc.properties.GrpcSpringProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-02-05 16:15
 */
@AutoConfiguration
@EnableConfigurationProperties(GrpcSpringProperties.class)
public class GrpcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientProperties grpcClientProperties(GrpcSpringProperties properties) {
		return SpringGrpcMapstruct.INSTANCE.client(properties, properties.getClient());
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerProperties grpcServerProperties(GrpcSpringProperties properties) {
		return SpringGrpcMapstruct.INSTANCE.server(properties, properties.getServer());
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientProvide grpcClientProvide(GrpcClientProperties properties, List<ClientInterceptor> interceptors) {
		return new GrpcClientProvide(properties, interceptors);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(GrpcServerBuilder.class)
	public GrpcServer grpcServer(GrpcServerBuilder builder) {
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientTraceIdInterceptor grpcClientTraceIdInterceptor(GrpcClientProperties properties) {
		return new GrpcClientTraceIdInterceptor(properties);
	}
}
