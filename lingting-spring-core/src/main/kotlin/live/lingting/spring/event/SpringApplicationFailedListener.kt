package live.lingting.spring.event

import live.lingting.framework.application.ApplicationHolder
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.ApplicationCloser
import org.springframework.boot.context.event.ApplicationFailedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author lingting 2023-12-06 17:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringApplicationFailedListener : ApplicationListener<ApplicationFailedEvent> {

    companion object {

        private val log = logger()

    }

    override fun onApplicationEvent(event: ApplicationFailedEvent) {
        ApplicationHolder.stop()
        log.debug("spring application failed")
        ApplicationCloser.stop(event.applicationContext)
    }
}
