package live.lingting.spring.event

import live.lingting.framework.application.ApplicationHolder
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.ApplicationCloser
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author lingting 2022/10/22 17:45
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringContextClosedListener : ApplicationListener<ContextClosedEvent> {

    companion object {

        private val log = logger()

    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        ApplicationHolder.stop()
        log.debug("spring context closed.")
        ApplicationCloser.stop(event.applicationContext)
    }

}
