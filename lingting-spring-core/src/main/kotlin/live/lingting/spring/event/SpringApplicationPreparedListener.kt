package live.lingting.spring.event

import live.lingting.framework.application.ApplicationHolder
import live.lingting.framework.util.Slf4jUtils.logger
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author lingting 2023-12-06 17:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringApplicationPreparedListener : ApplicationListener<ApplicationPreparedEvent> {
    companion object {
        private val log = logger()
    }

    override fun onApplicationEvent(event: ApplicationPreparedEvent) {
        log.debug("spring application prepared")
        ApplicationHolder.start()
    }
}
