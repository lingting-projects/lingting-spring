package live.lingting.spring.security.grpc.properties

import live.lingting.framework.properties.SecurityGrpcProperties
import live.lingting.spring.security.properties.SecuritySpringProperties
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-02-05 19:14
 */
@ConfigurationProperties(prefix = SecurityGrpcSpringProperties.PREFIX)
class SecurityGrpcSpringProperties {
    var authorizationKey: String = "Authorization"

    fun properties(): SecurityGrpcProperties {
        val properties = SecurityGrpcProperties()
        properties.authorizationKey = authorizationKey
        return properties
    }

    companion object {
        val PREFIX: String = SecuritySpringProperties.PREFIX + ".grpc"
    }
}
