package live.lingting.spring.post

import live.lingting.framework.context.ContextComponent
import live.lingting.framework.util.Slf4jUtils.logger

/**
 * @author lingting 2022/10/22 15:10
 */
class ContextComposeBeanPostProcessor : SpringBeanPostProcessor {
    override fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean {
        return bean is ContextComponent
    }

    override fun postProcessAfter(bean: Any, beanName: String): Any {
        log.trace("class [{}] start.", bean.javaClass)
        (bean as ContextComponent).onApplicationStart()
        return bean
    }

    companion object {
        private val log = logger()
    }
}
