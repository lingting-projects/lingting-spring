package live.lingting.spring.event

import live.lingting.framework.context.ContextHolder.start
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author lingting 2023-12-06 17:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringApplicationStartingListener : ApplicationListener<ApplicationStartingEvent> {
    override fun onApplicationEvent(event: ApplicationStartingEvent) {
        log.debug("spring application starting")
        start()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SpringApplicationStartingListener::class.java)
    }
}
