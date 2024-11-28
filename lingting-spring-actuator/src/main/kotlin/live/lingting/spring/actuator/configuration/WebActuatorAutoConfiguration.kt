package live.lingting.spring.actuator.configuration

import live.lingting.spring.actuator.health.WebHealthIndicator
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.server.WebServer
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

/**
 * @author lingting 2023-07-25 17:29
 */
@ConditionalOnWebApplication
@ConditionalOnClass(WebServer::class)
@AutoConfiguration(
    before = [ActuatorAutoConfiguration::class],
    after = []
)
open class WebActuatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledHealthIndicator("web")
    open fun webHealthIndicator(environment: Environment): WebHealthIndicator {
        return WebHealthIndicator(environment)
    }

}
