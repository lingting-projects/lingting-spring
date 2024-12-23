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
        val executor = ThreadUtils.executor()

        builder.status(if (!executor.isShutdown && !executor.isTerminated) Status.UP else Status.DOWN)

        if (executor is ThreadPoolExecutorServiceImpl) {
            builder.withDetail("corePoolSize", executor.corePoolSize)
                .withDetail("taskCount", executor.taskCount)
                .withDetail("activeCount", executor.activeCount)
                .withDetail("maximumPoolSize", executor.maximumPoolSize)
        }
    }
}
