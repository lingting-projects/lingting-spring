package live.lingting.spring.event

import live.lingting.framework.Sequence
import live.lingting.framework.application.ApplicationComponent
import live.lingting.framework.application.ApplicationHolder
import live.lingting.framework.util.Slf4jUtils.logger
import org.springframework.context.ApplicationContext
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
        val applicationContext = event.applicationContext
        log.debug("spring context closed.")
        // 上下文容器停止
        contextComponentStop(applicationContext)
    }

    fun contextComponentStop(applicationContext: ApplicationContext) {
        val map = applicationContext.getBeansOfType<ApplicationComponent>(ApplicationComponent::class.java)
        // 依照spring生态的@Order排序, 优先级高的先执行停止
        val values: MutableList<ApplicationComponent> = Sequence.asc<ApplicationComponent>(map.values)

        log.debug("context component stop before. size: {}", values.size)
        for (component in values) {
            log.trace("class [{}] stop before", component.javaClass)
            component.onApplicationStopBefore()
        }

        log.debug("context component stop. size: {}", values.size)
        for (component in values) {
            log.trace("class [{}] stop", component.javaClass)
            component.onApplicationStop()
        }
    }

}
