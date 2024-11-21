package live.lingting.spring.actuator.health

import live.lingting.framework.grpc.GrpcServer.isRunning
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status

/**
 * @author lingting 2023-07-25 14:35
 */
class ThreadPoolHealthIndicator : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        val instance: ThreadPool.Impl = instance()

        builder.status(if (instance.isRunning()) Status.UP else Status.DOWN)
            .withDetail("CorePoolSize", instance.getCorePoolSize())
            .withDetail("TaskCount", instance.getTaskCount())
            .withDetail("ActiveCount", instance.getActiveCount())
            .withDetail("MaximumPoolSize", instance.getMaximumPoolSize())
    }
}
