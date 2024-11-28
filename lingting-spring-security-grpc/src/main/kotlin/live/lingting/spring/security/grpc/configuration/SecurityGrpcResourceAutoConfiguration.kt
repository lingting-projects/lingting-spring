package live.lingting.spring.security.grpc.configuration

import live.lingting.framework.grpc.GrpcClientProvide
import live.lingting.framework.security.authorize.SecurityAuthorize
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.grpc.convert.SecurityGrpcConvert
import live.lingting.framework.security.grpc.interceptor.SecurityGrpcRemoteResourceClientInterceptor
import live.lingting.framework.security.grpc.interceptor.SecurityGrpcResourceServerInterceptor
import live.lingting.framework.security.grpc.properties.SecurityGrpcProperties
import live.lingting.framework.security.grpc.resource.SecurityTokenGrpcRemoteResolver
import live.lingting.framework.security.properties.SecurityProperties
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.spring.grpc.configuration.GrpcAutoConfiguration
import live.lingting.spring.security.conditional.ConditionalOnResource
import live.lingting.spring.security.conditional.ConditionalOnUsingRemoteAuthorization
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

@ConditionalOnResource
@AutoConfiguration(after = [GrpcAutoConfiguration::class], beforeName = ["SecurityWebResourceAutoConfiguration"])
open class SecurityGrpcResourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun securityGrpcResourceServerInterceptor(
        properties: SecurityGrpcProperties,
        service: SecurityResourceService,
        authorize: SecurityAuthorize,
        convert: SecurityConvert,
    ): SecurityGrpcResourceServerInterceptor {
        return SecurityGrpcResourceServerInterceptor(properties.authorizationKey(), service, authorize, convert)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnUsingRemoteAuthorization
    open fun securityGrpcRemoteResourceClientInterceptor(
        properties: SecurityGrpcProperties
    ): SecurityGrpcRemoteResourceClientInterceptor {
        return SecurityGrpcRemoteResourceClientInterceptor(properties)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnUsingRemoteAuthorization
    @ConditionalOnBean(GrpcClientProvide::class)
    open fun securityTokenGrpcRemoteResolver(
        properties: SecurityProperties,
        interceptor: SecurityGrpcRemoteResourceClientInterceptor, provide: GrpcClientProvide,
        convert: SecurityGrpcConvert
    ): SecurityTokenGrpcRemoteResolver {
        val authorization = properties.authorization
        val channel = provide.builder(authorization.remoteHost!!)
            .provide()
            .interceptor(interceptor)
            .build()
        return SecurityTokenGrpcRemoteResolver(channel, convert)
    }
}
