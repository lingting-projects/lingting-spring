package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch._types.Time
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.BulkResponse
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse
import co.elastic.clients.elasticsearch.core.ScrollRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest
import co.elastic.clients.elasticsearch.core.UpdateRequest
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import java.util.function.UnaryOperator
import live.lingting.framework.api.ScrollResult
import live.lingting.framework.elasticsearch.IndexInfo
import live.lingting.framework.elasticsearch.api.ElasticsearchApi
import live.lingting.framework.elasticsearch.api.ElasticsearchApiImpl
import live.lingting.framework.elasticsearch.builder.Compare
import live.lingting.framework.elasticsearch.builder.SearchBuilder
import live.lingting.framework.elasticsearch.builder.UpdateBuilder
import live.lingting.framework.elasticsearch.retry.ElasticsearchRetryProperties
import live.lingting.framework.elasticsearch.util.ElasticsearchUtils.getEntityClass
import live.lingting.framework.function.ThrowingSupplier
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.framework.value.WaitValue

/**
 * @author lingting 2024-03-08 16:43
 */
abstract class AbstractElasticsearchServiceImpl<T : Any> : ElasticsearchApi<T> {

    override val log = logger()

    open val cls = getEntityClass<T>(javaClass)

    private val apiValue = WaitValue.of<ElasticsearchApiImpl<T>>()

    var api: ElasticsearchApiImpl<T>
        get() = apiValue.notNull()
        set(value) {
            apiValue.update(value)
        }

    val info: IndexInfo
        get() = api.info

    override val scrollSize: Long
        get() = api.scrollSize

    override val scrollTime: Time?
        get() = api.scrollTime

    override fun documentId(t: T): String? {
        return api.documentId(t)
    }

    override fun index(): Collection<String> {
        return api.index()
    }

    override fun index(t: T): String {
        return api.index(t)
    }

    override fun <R> retry(supplier: ThrowingSupplier<R>): R? {
        return api.retry(supplier)
    }

    override fun <R> retry(properties: ElasticsearchRetryProperties?, supplier: ThrowingSupplier<R>): R? {
        return api.retry(properties, supplier)
    }

    override fun merge(builder: Compare<T, *>) {
        api.merge(builder)
    }

    override fun getByDoc(documentId: String): T? {
        return api.getByDoc(documentId)
    }

    override fun search(builder: SearchBuilder<T>, operator: UnaryOperator<SearchRequest.Builder>): SearchResponse<T> {
        return api.search(builder, operator)
    }

    override fun scroll(scrollId: String?, operator: UnaryOperator<ScrollRequest.Builder>): ScrollResult<T, String> {
        return api.scroll(scrollId, operator)
    }

    override fun scrollClear(scrollId: String?) {
        api.scrollClear(scrollId)
    }

    override fun update(operator: UnaryOperator<UpdateRequest.Builder<T, T>>, documentId: String?): Boolean {
        return api.update(operator, documentId)
    }

    override fun updateByQuery(builder: UpdateBuilder<T>, operator: UnaryOperator<UpdateByQueryRequest.Builder>): Long {
        return api.updateByQuery(builder, operator)
    }

    override fun deleteByQuery(builder: SearchBuilder<T>, operator: UnaryOperator<DeleteByQueryRequest.Builder>): DeleteByQueryResponse {
        return api.deleteByQuery(builder, operator)
    }

    override fun bulk(operations: List<BulkOperation>, operator: UnaryOperator<BulkRequest.Builder>): BulkResponse {
        return api.bulk(operations, operator)
    }

}
