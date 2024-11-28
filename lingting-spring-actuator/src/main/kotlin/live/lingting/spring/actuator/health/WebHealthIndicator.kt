package live.lingting.spring.actuator.health

import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.core.env.Environment

/**
 * @author lingting 2024/11/28 16:19
 */
class WebHealthIndicator(val environment: Environment) : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        builder.status(Status.UP)
            .withDetail("port", environment.getProperty("local.server.port") ?: "-1")
    }

}
