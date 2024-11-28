package live.lingting.spring.actuator.health

import live.lingting.framework.thread.ThreadPool
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status

/**
 * @author lingting 2023-07-25 14:35
 */
class ThreadPoolHealthIndicator : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        val instance = ThreadPool.instance

        builder.status(if (instance.isRunning) Status.UP else Status.DOWN)
            .withDetail("corePoolSize", instance.corePoolSize)
            .withDetail("taskCount", instance.taskCount)
            .withDetail("activeCount", instance.activeCount)
            .withDetail("maximumPoolSize", instance.maximumPoolSize)
    }
}
