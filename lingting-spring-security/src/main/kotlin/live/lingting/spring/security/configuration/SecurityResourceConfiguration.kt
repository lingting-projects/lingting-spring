package live.lingting.spring.security.configuration

import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import live.lingting.framework.security.authorize.SecurityAuthorize
import live.lingting.framework.security.properties.SecurityProperties
import live.lingting.framework.security.resolver.SecurityTokenDefaultResolver
import live.lingting.framework.security.resolver.SecurityTokenResolver
import live.lingting.framework.security.resource.SecurityDefaultResourceServiceImpl
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.framework.security.store.SecurityMemoryStore
import live.lingting.framework.security.store.SecurityStore
import live.lingting.spring.security.conditional.ConditionalOnUsingLocalAuthorization
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2023-03-29 21:09
 */
class SecurityResourceConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun securityAuthorize(properties: SecurityProperties): SecurityAuthorize {
        return SecurityAuthorize(properties.order)
    }

    @Bean
    @ConditionalOnMissingBean
    fun securityStoreResourceService(resolvers: MutableList<SecurityTokenResolver>): SecurityResourceService {
        return SecurityDefaultResourceServiceImpl(resolvers)
    }

    @Bean
    @ConditionalOnUsingLocalAuthorization
    fun securityTokenDefaultResolver(provider: ObjectProvider<SecurityStore>): SecurityTokenDefaultResolver {
        val store = AtomicReference<SecurityStore>(SecurityMemoryStore())
        provider.ifAvailable(Consumer { newValue: SecurityStore -> store.set(newValue) })
        return SecurityTokenDefaultResolver(store.get()!!)
    }
}
