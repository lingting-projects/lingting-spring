package live.lingting.spring.security.configuration

import live.lingting.framework.security.SecurityEndpointService
import live.lingting.framework.security.authorize.SecurityAuthorizationService
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.framework.security.resolver.SecurityTokenResolverRegistry
import live.lingting.framework.security.store.SecurityMemoryStore
import live.lingting.framework.security.store.SecurityStore
import live.lingting.spring.security.password.SecurityDefaultPassword
import live.lingting.spring.security.properties.SecuritySpringProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2023-04-06 15:56
 */
open class SecurityAuthorizationConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun securityStore(): SecurityStore {
        return SecurityMemoryStore()
    }

    @Bean
    @ConditionalOnMissingBean
    fun securityEndpointService(
        service: SecurityAuthorizationService,
        store: SecurityStore,
        password: SecurityPassword,
        convert: SecurityConvert,
        registry: SecurityTokenResolverRegistry,
    ): SecurityEndpointService {
        return SecurityEndpointService(service, store, password, convert, registry)
    }

    /**
     * 使用 password-secret-key 可以匹配 - 和驼峰
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        prefix = SecuritySpringProperties.PREFIX + ".authorization",
        name = ["password-secret-key"],
        matchIfMissing = true,
    )
    fun securityPassword(properties: SecuritySpringProperties): SecurityPassword {
        val authorization = properties.authorization
        return SecurityDefaultPassword(authorization.passwordSecretKey)
    }
}
