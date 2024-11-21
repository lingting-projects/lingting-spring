package live.lingting.spring.security.grpc.configuration

import live.lingting.framework.convert.SecurityGrpcConvert
import live.lingting.framework.endpoint.SecurityGrpcAuthorizationEndpoint
import live.lingting.framework.protobuf.SecurityGrpcAuthorizationServiceGrpc.SecurityGrpcAuthorizationServiceImplBase
import live.lingting.framework.security.authorize.SecurityAuthorizationService
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.framework.security.store.SecurityStore
import live.lingting.spring.security.conditional.ConditionalOnAuthorization
import live.lingting.spring.security.configuration.SecurityAuthorizationConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-05 19:23
 */
@ConditionalOnAuthorization
@AutoConfiguration(before = [SecurityAuthorizationConfiguration::class])
class SecurityGrpcAuthorizationAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun securityGrpcAuthorizationServiceImplBase(
        service: SecurityAuthorizationService, store: SecurityStore, password: SecurityPassword,
        convert: SecurityGrpcConvert
    ): SecurityGrpcAuthorizationServiceImplBase {
        return SecurityGrpcAuthorizationEndpoint(service, store, password, convert)
    }
}
