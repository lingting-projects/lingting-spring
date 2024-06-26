package live.lingting.spring.grpc.configuration;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import live.lingting.framework.grpc.GrpcServerBuilder;
import live.lingting.framework.grpc.interceptor.GrpcServerTraceIdInterceptor;
import live.lingting.framework.grpc.properties.GrpcServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-02-05 16:23
 */
public class GrpcServerConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerBuilder grpcServerBuilder(GrpcServerProperties properties, List<BindableService> services,
			List<ServerInterceptor> interceptors) {
		return new GrpcServerBuilder().properties(properties).service(services).interceptor(interceptors);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerTraceIdInterceptor grpcServerTraceIdInterceptor(GrpcServerProperties properties) {
		return new GrpcServerTraceIdInterceptor(properties);
	}

}
