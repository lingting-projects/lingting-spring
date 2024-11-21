package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import java.util.function.Function
import live.lingting.framework.elasticsearch.ElasticsearchApi
import live.lingting.framework.elasticsearch.ElasticsearchProperties
import live.lingting.framework.elasticsearch.ElasticsearchProvider.api
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataPermissionHandler
import live.lingting.spring.post.SpringBeanPostProcessor

/**
 * @author lingting 2024-03-08 16:52
 */
class ElasticsearchServiceImplBeanPost(
    private val properties: ElasticsearchProperties, private val client: ElasticsearchClient,
    private val handler: ElasticsearchDataPermissionHandler
) : SpringBeanPostProcessor {
    override fun isProcess(bean: Any, beanName: String, isBefore: Boolean): Boolean {
        return bean is AbstractElasticsearchServiceImpl<*>
    }

    override fun postProcessAfter(bean: Any, beanName: String): Any {
        if (bean is AbstractElasticsearchServiceImpl<*>) {
            val api: ElasticsearchApi<*> = api<T>(
                bean.index!!, bean.cls, Function { t -> bean.documentId(t) }, properties,
                handler, client
            )
            bean.setApi(api)
        }
        return bean
    }
}
