package live.lingting.spring.event

import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.util.SpringUtils
import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * 监听spring上下文初始化完成事件
 * @author lingting 2022/10/15 15:27
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringApplicationContextInitializedListener : ApplicationListener<ApplicationContextInitializedEvent> {

    companion object {
        private val log = logger()
    }

    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        log.debug("spring application context initialized")
        // 给 spring utils 注入 spring 上下文
        SpringUtils.context = event.applicationContext
    }

}
