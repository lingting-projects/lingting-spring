package live.lingting.spring.grpc.configuration

import io.grpc.ClientInterceptor
import live.lingting.framework.grpc.GrpcClientProvide
import live.lingting.framework.grpc.GrpcServer
import live.lingting.framework.grpc.GrpcServerBuilder
import live.lingting.framework.grpc.interceptor.GrpcClientTraceIdInterceptor
import live.lingting.framework.grpc.properties.GrpcClientProperties
import live.lingting.framework.grpc.properties.GrpcServerProperties
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
open class GrpcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun grpcClientProperties(properties: GrpcSpringProperties): GrpcClientProperties {
        val client = GrpcClientProperties()
        client.host = properties.client.host
        client.port = properties.client.port
        client.traceIdKey = properties.traceIdKey
        client.traceOrder = properties.traceOrder
        client.usePlaintext = properties.client.usePlaintext
        client.disableSsl = properties.client.disableSsl
        client.enableRetry = properties.client.enableRetry
        client.enableKeepAlive = properties.client.enableKeepAlive
        client.keepAliveTime = properties.keepAliveTime
        client.keepAliveTimeout = properties.keepAliveTimeout
        return client
    }

    @Bean
    @ConditionalOnMissingBean
    open fun grpcServerProperties(properties: GrpcSpringProperties): GrpcServerProperties {
        val server = GrpcServerProperties()
        server.port = properties.server.port
        server.messageSize = properties.server.messageSize
        server.keepAliveTime = properties.keepAliveTime
        server.keepAliveTimeout = properties.keepAliveTimeout
        server.traceIdKey = properties.traceIdKey
        server.traceOrder = properties.traceOrder
        server.exceptionHandlerOrder = properties.server.exceptionHandlerOrder
        return server
    }

    @Bean
    @ConditionalOnMissingBean
    open fun grpcClientProvide(properties: GrpcClientProperties, interceptors: MutableList<ClientInterceptor>): GrpcClientProvide {
        return GrpcClientProvide(properties, interceptors)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(GrpcServerBuilder::class)
    open fun grpcServer(builder: GrpcServerBuilder): GrpcServer {
        return builder.build()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun grpcClientTraceIdInterceptor(properties: GrpcClientProperties): GrpcClientTraceIdInterceptor {
        return GrpcClientTraceIdInterceptor(properties)
    }
}
