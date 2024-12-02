package live.lingting.spring.post

import live.lingting.framework.application.ApplicationComponent
import live.lingting.framework.util.Slf4jUtils.logger

/**
 * @author lingting 2022/10/22 15:10
 */
class ApplicationComponentBeanPostProcessor : SpringBeanPostProcessor {
    override fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean {
        return bean is ApplicationComponent
    }

    override fun postProcessAfter(bean: Any, beanName: String): Any {
        log.trace("class [{}] start.", bean.javaClass)
        (bean as ApplicationComponent).onApplicationStart()
        return bean
    }

    companion object {
        private val log = logger()
    }
}
