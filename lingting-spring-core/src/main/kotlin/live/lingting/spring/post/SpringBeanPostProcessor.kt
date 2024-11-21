package live.lingting.spring.post

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.Ordered

/**
 * @author lingting 2022/10/22 15:11
 */
interface SpringBeanPostProcessor : BeanPostProcessor, Ordered {
    /**
     * 判断是否处理该bean
     * @param bean bean实例
     * @param beanName bean名称
     * @param isBefore true表示当前为初始化之前是否处理判断, false 表示当前为初始化之后是否处理判断
     * @return boolean true 表示要处理该bean
     */
    fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean

    /**
     * 初始化之前处理
     * @param bean bean
     * @param beanName bean名称
     * @return java.lang.Object
     */
    fun postProcessBefore(bean: Any, beanName: String): Any {
        return super<BeanPostProcessor>.postProcessBeforeInitialization(bean, beanName)
    }

    /**
     * 初始化之后处理
     * @param bean bean
     * @param beanName bean名称
     * @return java.lang.Object
     */
    fun postProcessAfter(bean: Any, beanName: String): Any {
        return super<BeanPostProcessor>.postProcessAfterInitialization(bean, beanName)
    }

    override fun getOrder(): Int {
        // 默认优先级
        return Ordered.HIGHEST_PRECEDENCE
    }


    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (isProcess(bean, beanName, true)) {
            return postProcessBefore(bean, beanName)
        }

        return super<BeanPostProcessor>.postProcessBeforeInitialization(bean, beanName)
    }


    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (isProcess(bean, beanName, false)) {
            return postProcessAfter(bean, beanName)
        }
        return super<BeanPostProcessor>.postProcessAfterInitialization(bean, beanName)
    }
}
