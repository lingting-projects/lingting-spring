package live.lingting.spring.actuator.health

import live.lingting.framework.thread.executor.ThreadPoolExecutorServiceImpl
import live.lingting.framework.util.ThreadUtils
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status

/**
 * @author lingting 2023-07-25 14:35
 */
class ThreadPoolHealthIndicator : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        val instance = ThreadUtils.instance()

        builder.status(if (instance.isRunning) Status.UP else Status.DOWN)

        if (instance is ThreadPoolExecutorServiceImpl) {
            builder.withDetail("corePoolSize", instance.corePoolSize)
                .withDetail("taskCount", instance.taskCount)
                .withDetail("activeCount", instance.activeCount)
                .withDetail("maximumPoolSize", instance.maximumPoolSize)
        }
    }
}
