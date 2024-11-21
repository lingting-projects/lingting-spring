package live.lingting.spring.grpc.configuration

import live.lingting.framework.grpc.exception.GrpcExceptionInstance
import live.lingting.framework.grpc.exception.GrpcExceptionProcessor
import live.lingting.framework.grpc.interceptor.GrpcServerExceptionInterceptor
import live.lingting.framework.grpc.properties.GrpcServerProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-03-27 10:14
 */
class GrpcExceptionConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun grpcExceptionProcessor(instances: MutableList<GrpcExceptionInstance>): GrpcExceptionProcessor {
        return GrpcExceptionProcessor(instances)
    }

    @Bean
    @ConditionalOnMissingBean
    fun grpcServerExceptionInterceptor(
        processor: GrpcExceptionProcessor,
        properties: GrpcServerProperties
    ): GrpcServerExceptionInterceptor {
        return GrpcServerExceptionInterceptor(properties, processor)
    }
}
