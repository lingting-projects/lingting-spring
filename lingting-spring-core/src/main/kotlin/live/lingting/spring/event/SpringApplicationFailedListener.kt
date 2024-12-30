package live.lingting.spring.event

import live.lingting.framework.application.ApplicationHolder
import live.lingting.framework.util.Slf4jUtils.logger
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author lingting 2023-12-06 17:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringApplicationFailedListener : ApplicationListener<ApplicationStartingEvent> {

    companion object {
        private val log = logger()
    }

    override fun onApplicationEvent(event: ApplicationStartingEvent) {
        log.debug("spring application failed")
        ApplicationHolder.stop()
    }
}
