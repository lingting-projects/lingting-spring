package live.lingting.spring.grpc.configuration

import io.grpc.ClientInterceptor
import live.lingting.framework.grpc.GrpcClientProvide
import live.lingting.framework.grpc.GrpcServer
import live.lingting.framework.grpc.GrpcServerBuilder
import live.lingting.framework.grpc.interceptor.GrpcClientTraceIdInterceptor
import live.lingting.framework.grpc.properties.GrpcClientProperties
import live.lingting.framework.grpc.properties.GrpcServerProperties
import live.lingting.spring.grpc.mapstruct.SpringGrpcMapstruct
import live.lingting.spring.grpc.properties.GrpcSpringProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-05 16:15
 */
@AutoConfiguration
@EnableConfigurationProperties(GrpcSpringProperties::class)
class GrpcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun grpcClientProperties(properties: GrpcSpringProperties): GrpcClientProperties {
        return SpringGrpcMapstruct.INSTANCE.client(properties, properties.getClient())
    }

    @Bean
    @ConditionalOnMissingBean
    fun grpcServerProperties(properties: GrpcSpringProperties): GrpcServerProperties {
        return SpringGrpcMapstruct.INSTANCE.server(properties, properties.getServer())
    }

    @Bean
    @ConditionalOnMissingBean
    fun grpcClientProvide(properties: GrpcClientProperties, interceptors: MutableList<ClientInterceptor>): GrpcClientProvide {
        return GrpcClientProvide(properties, interceptors)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(GrpcServerBuilder::class)
    fun grpcServer(builder: GrpcServerBuilder): GrpcServer {
        return builder.build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun grpcClientTraceIdInterceptor(properties: GrpcClientProperties): GrpcClientTraceIdInterceptor {
        return GrpcClientTraceIdInterceptor(properties)
    }
}
