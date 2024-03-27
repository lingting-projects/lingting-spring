package live.lingting.spring.grpc.configuration;

import live.lingting.framework.grpc.exception.GrpcExceptionInstance;
import live.lingting.framework.grpc.exception.GrpcExceptionProcessor;
import live.lingting.framework.grpc.interceptor.GrpcServerExceptionInterceptor;
import live.lingting.framework.grpc.properties.GrpcServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-03-27 10:14
 */
public class GrpcExceptionConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GrpcExceptionProcessor grpcExceptionProcessor(List<GrpcExceptionInstance> instances) {
		return new GrpcExceptionProcessor(instances);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerExceptionInterceptor grpcServerExceptionInterceptor(GrpcExceptionProcessor processor,
			GrpcServerProperties properties) {
		return new GrpcServerExceptionInterceptor(properties, processor);
	}

}
