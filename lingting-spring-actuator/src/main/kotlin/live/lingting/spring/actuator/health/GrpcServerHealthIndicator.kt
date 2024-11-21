package live.lingting.spring.actuator.health

import live.lingting.framework.grpc.GrpcServer
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status

/**
 * @author lingting 2023-11-23 21:29
 */
class GrpcServerHealthIndicator(private val server: GrpcServer) : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        builder.status(if (server.isRunning) Status.UP else Status.DOWN)
            .withDetail("Port", server.port().toString())
    }
}
