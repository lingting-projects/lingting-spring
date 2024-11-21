package live.lingting.spring.mdc

import org.slf4j.MDC
import org.springframework.core.task.TaskDecorator

/**
 * @author lingting 2022/11/1 16:04
 */
class MdcTaskDecorator : TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val copyOfContextMap = MDC.getCopyOfContextMap() ?: emptyMap<String, String>()
        return Runnable {
            try {
                MDC.setContextMap(copyOfContextMap)
                runnable.run()
            } finally {
                MDC.clear()
            }
        }
    }
}
