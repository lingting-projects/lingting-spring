package live.lingting.spring

import live.lingting.framework.Sequence
import live.lingting.framework.application.ApplicationComponent
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.util.SpringUtils
import org.springframework.context.ApplicationContext

/**
 * @author lingting 2025/6/21 18:30
 */
object ApplicationCloser {

    private val log = logger()

    fun stop() = stop(SpringUtils.context)

    fun stop(context: ApplicationContext?) {
        val beans = context?.getBeansOfType(ApplicationComponent::class.java)
        stop(beans?.values)
    }

    fun stop(collection: Collection<ApplicationComponent>?) {
        if (collection.isNullOrEmpty()) {
            return
        }
        // 依照spring生态的@Order排序, 优先级高的先执行停止
        val values = Sequence.asc<ApplicationComponent>(collection)

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
