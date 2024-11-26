package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import live.lingting.framework.elasticsearch.ElasticsearchProperties
import live.lingting.framework.elasticsearch.interceptor.Interceptor
import live.lingting.framework.elasticsearch.polymerize.PolymerizeFactory
import live.lingting.spring.post.SpringBeanPostProcessor

/**
 * @author lingting 2024-03-08 16:52
 */
class ElasticsearchServiceImplBeanPost(
    val properties: ElasticsearchProperties,
    val client: ElasticsearchClient,
    val interceptors: List<Interceptor>,
    val polymerizeFactory: PolymerizeFactory,
) : SpringBeanPostProcessor {
    override fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean {
        return bean is AbstractElasticsearchServiceImpl<*>
    }

    override fun postProcessAfter(bean: Any, beanName: String): Any {
        if (bean is AbstractElasticsearchServiceImpl<*>) {
            bean.newApi(properties, client, interceptors, polymerizeFactory)
        }
        return bean
    }
}
