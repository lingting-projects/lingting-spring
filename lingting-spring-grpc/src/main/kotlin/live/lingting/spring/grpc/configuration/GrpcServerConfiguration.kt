package live.lingting.spring.grpc.configuration

import io.grpc.BindableService
import io.grpc.ServerInterceptor
import live.lingting.framework.grpc.GrpcServerBuilder
import live.lingting.framework.grpc.customizer.ServerCustomizer
import live.lingting.framework.grpc.interceptor.GrpcServerTraceIdInterceptor
import live.lingting.framework.grpc.properties.GrpcServerProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-05 16:23
 */
open class GrpcServerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun grpcServerBuilder(
        properties: GrpcServerProperties,
        services: Collection<BindableService>,
        interceptors: Collection<ServerInterceptor>,
        customizers: Collection<ServerCustomizer>
    ): GrpcServerBuilder {
        return GrpcServerBuilder(customizers).properties(properties).service(services).interceptor(interceptors)
    }

    @Bean
    @ConditionalOnMissingBean
    fun grpcServerTraceIdInterceptor(properties: GrpcServerProperties): GrpcServerTraceIdInterceptor {
        return GrpcServerTraceIdInterceptor(properties)
    }

}
