package live.lingting.spring.security.web.properties

import live.lingting.spring.security.properties.SecuritySpringProperties
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-03-20 19:50
 */
@ConfigurationProperties(SecurityWebProperties.PREFIX)
class SecurityWebProperties {
    companion object {
        const val PREFIX: String = SecuritySpringProperties.PREFIX + ".web"
    }

    var ignoreUris: Set<String> = emptySet()

    @JvmField
    var headerAuthorization: String = "Authorization"

    var paramAuthorization: String = "token"

}
