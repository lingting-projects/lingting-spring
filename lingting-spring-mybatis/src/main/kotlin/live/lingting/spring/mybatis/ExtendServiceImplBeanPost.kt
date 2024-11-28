package live.lingting.spring.mybatis

import live.lingting.framework.mybatis.extend.ExtendMapper
import live.lingting.framework.mybatis.extend.ExtendServiceImpl
import live.lingting.spring.post.SpringBeanPostProcessor
import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024-03-12 17:41
 */
@Suppress("UNCHECKED_CAST")
class ExtendServiceImplBeanPost : SpringBeanPostProcessor {

    override fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean {
        return bean is ExtendServiceImpl<*, *>
    }

    override fun postProcessAfter(bean: Any, beanName: String): Any {
        if (bean is ExtendServiceImpl<*, *>) {
            val impl = bean as ExtendServiceImpl<ExtendMapper<Any>, Any>
            val mapperClass = impl.mapperClass
            val mapper = SpringUtils.getBean(mapperClass)
            impl.mapper = mapper
        }
        return bean
    }
}
