package live.lingting.spring.security.configuration

import live.lingting.framework.security.authorize.SecurityAuthorizationCustomizer
import live.lingting.framework.security.authorize.SecurityAuthorize
import live.lingting.framework.security.properties.SecurityProperties
import live.lingting.framework.security.resolver.SecurityTokenDefaultResolver
import live.lingting.framework.security.resolver.SecurityTokenResolverRegistry
import live.lingting.framework.security.resource.SecurityDefaultResourceServiceImpl
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.framework.security.store.SecurityMemoryStore
import live.lingting.framework.security.store.SecurityStore
import live.lingting.spring.security.conditional.ConditionalOnUsingLocalAuthorization
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import java.util.concurrent.atomic.AtomicReference

/**
 * @author lingting 2023-03-29 21:09
 */
open class SecurityResourceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun securityAuthorize(
        properties: SecurityProperties,
        customizers: List<SecurityAuthorizationCustomizer>
    ): SecurityAuthorize {
        return SecurityAuthorize(properties.order, customizers)
    }

    @Bean
    @ConditionalOnMissingBean
    fun securityStoreResourceService(registry: SecurityTokenResolverRegistry): SecurityResourceService {
        return SecurityDefaultResourceServiceImpl(registry)
    }

    @Bean
    @ConditionalOnUsingLocalAuthorization
    fun securityTokenDefaultResolver(provider: ObjectProvider<SecurityStore>): SecurityTokenDefaultResolver {
        val store = AtomicReference<SecurityStore>(SecurityMemoryStore())
        provider.ifAvailable { newValue: SecurityStore -> store.set(newValue) }
        return SecurityTokenDefaultResolver(store.get()!!)
    }
}
