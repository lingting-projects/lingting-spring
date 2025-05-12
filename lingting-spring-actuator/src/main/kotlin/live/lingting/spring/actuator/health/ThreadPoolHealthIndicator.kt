package live.lingting.spring.actuator.health

import live.lingting.framework.thread.executor.DelegationExecutorService
import live.lingting.framework.thread.platform.PlatformThread
import live.lingting.framework.thread.virtual.VirtualThread
import live.lingting.framework.util.ThreadUtils
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author lingting 2023-07-25 14:35
 */
class ThreadPoolHealthIndicator : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        builder.status(if (ThreadUtils.isRunning) Status.UP else Status.DOWN)

        if (VirtualThread.isSupport) {
            detail("platform-", builder, PlatformThread)
            detail("virtual-", builder, VirtualThread)
        } else {
            detail("", builder, PlatformThread)
        }
    }


    fun detail(prefix: String, builder: Health.Builder, executor: Executor) {
        if (executor is ExecutorService && prefix.isNotBlank()) {
            builder.withDetail("${prefix}running", !(executor.isShutdown || executor.isTerminated))
        }

        val threadPoolExecutor = executor as? ThreadPoolExecutor
            ?: if (executor is DelegationExecutorService) executor.find(ThreadPoolExecutor::class.java)
            else null

        if (threadPoolExecutor != null) {
            builder.withDetail("${prefix}corePoolSize", threadPoolExecutor.corePoolSize)
                .withDetail("${prefix}taskCount", threadPoolExecutor.taskCount)
                .withDetail("${prefix}activeCount", threadPoolExecutor.activeCount)
                .withDetail("${prefix}maximumPoolSize", threadPoolExecutor.maximumPoolSize)
        }

    }

}
