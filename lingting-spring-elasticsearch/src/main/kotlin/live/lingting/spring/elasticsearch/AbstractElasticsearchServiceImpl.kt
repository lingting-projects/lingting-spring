package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch._types.Script
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.BulkResponse
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse
import co.elastic.clients.elasticsearch.core.ScrollRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse
import co.elastic.clients.elasticsearch.core.UpdateRequest
import co.elastic.clients.elasticsearch.core.UpdateResponse
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationBase
import co.elastic.clients.elasticsearch.core.search.HitsMetadata
import co.elastic.clients.util.ObjectBuilder
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator
import live.lingting.framework.api.LimitCursor
import live.lingting.framework.api.PaginationParams
import live.lingting.framework.api.PaginationResult
import live.lingting.framework.api.ScrollCursor
import live.lingting.framework.api.ScrollParams
import live.lingting.framework.api.ScrollResult
import live.lingting.framework.elasticsearch.ElasticsearchApi
import live.lingting.framework.elasticsearch.ElasticsearchUtils.getEntityClass
import live.lingting.framework.elasticsearch.IndexInfo
import live.lingting.framework.elasticsearch.builder.QueryBuilder
import live.lingting.framework.elasticsearch.builder.ScriptBuilder
import live.lingting.framework.function.ThrowingRunnable
import live.lingting.framework.function.ThrowingSupplier
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.framework.value.WaitValue

/**
 * @author lingting 2024-03-08 16:43
 */
abstract class AbstractElasticsearchServiceImpl<T : Any> {

    protected val log = logger()

    open val cls = getEntityClass<T>(javaClass)

    private val apiValue = WaitValue.of<ElasticsearchApi<T>>()

    var api: ElasticsearchApi<T>
        get() = apiValue.notNull()
        set(value) {
            apiValue.update(value)
        }

    val info: IndexInfo
        get() = api.info

    open fun documentId(t: T): String? = null

    // region extend
    open fun script(): ScriptBuilder<T> {
        return ScriptBuilder.builder<T>()
    }

    open fun query(): QueryBuilder<T> {
        return QueryBuilder.builder()
    }

    open fun tryIgnore(runnable: ThrowingRunnable) {
        try {
            runnable.run()
        } catch (e: Throwable) {
            log.warn("ignore error: {}", e.message)
        }
    }

    open fun <R> tryElse(supplier: ThrowingSupplier<R>, defaultValue: R): R {
        return tryGet<R>(supplier, Supplier { defaultValue })
    }

    open fun <R> tryGet(supplier: ThrowingSupplier<R>, defaultValue: Supplier<R>): R {
        return tryGet<R>(supplier, Function { e -> defaultValue.get() })
    }

    open fun <R> tryGet(supplier: ThrowingSupplier<R>, defaultValue: Function<Throwable, R>): R {
        try {
            return supplier.get()
        } catch (e: Throwable) {
            log.warn("try error: {}", e.message)
            return defaultValue.apply(e)
        }
    }

    // endregion

    open fun retry(runnable: ThrowingRunnable) {
        api.retry(runnable)
    }

    open fun <R> retry(supplier: ThrowingSupplier<R>): R {
        return api.retry<R>(supplier)
    }

    open fun merge(vararg arrays: Query): Query {
        return api.merge(*arrays)
    }

    open fun merge(builder: QueryBuilder<T>): Query {
        return api.merge(builder)
    }

    open fun get(id: String): T {
        return api.get(id)
    }

    open fun getByQuery(vararg queries: Query): T? {
        return api.getByQuery(*queries)
    }

    open fun getByQuery(queries: QueryBuilder<T>): T? {
        return api.getByQuery(queries)
    }

    open fun getByQuery(operator: UnaryOperator<SearchRequest.Builder>, queries: QueryBuilder<T>): T? {
        return api.getByQuery(operator, queries)
    }

    open fun count(vararg queries: Query): Long {
        return api.count(*queries)
    }

    open fun count(queries: QueryBuilder<T>): Long {
        return api.count(queries)
    }

    open fun search(vararg queries: Query): HitsMetadata<T> {
        return api.search(*queries)
    }

    open fun search(queries: QueryBuilder<T>): HitsMetadata<T> {
        return api.search(queries)
    }

    open fun search(operator: UnaryOperator<SearchRequest.Builder>, queries: QueryBuilder<T>): HitsMetadata<T> {
        return api.search(operator, queries)
    }

    open fun <E> search(
        operator: UnaryOperator<SearchRequest.Builder>, queries: QueryBuilder<T>,
        convert: Function<SearchResponse<T>, E>
    ): E {
        return api.search(operator, queries, convert)
    }

    open fun page(params: PaginationParams): PaginationResult<T> {
        return api.page(params, QueryBuilder.builder())
    }

    open fun page(params: PaginationParams, queries: QueryBuilder<T>): PaginationResult<T> {
        return api.page(params, queries)
    }

    open fun aggs(
        consumer: BiConsumer<String, Aggregate>, aggregationMap: Map<String, Aggregation>,
        queries: QueryBuilder<T>
    ) {
        api.aggs(consumer, aggregationMap, queries)
    }

    open fun aggs(
        operator: UnaryOperator<SearchRequest.Builder>, consumer: BiConsumer<String, Aggregate>,
        aggregationMap: Map<String, Aggregation>, queries: QueryBuilder<T>
    ) {
        api.aggs(operator, consumer, aggregationMap, queries)
    }

    open fun aggs(
        operator: UnaryOperator<SearchRequest.Builder>, consumer: Consumer<SearchResponse<T>>,
        aggregationMap: Map<String, Aggregation>, queries: QueryBuilder<T>
    ) {
        api.aggs(operator, consumer, aggregationMap, queries)
    }

    open fun update(documentId: String?, scriptOperator: Function<Script.Builder, ObjectBuilder<Script>>): Boolean {
        return api.update(documentId, scriptOperator)
    }

    open fun update(documentId: String?, script: Script): Boolean {
        return api.update(documentId, script)
    }

    open fun update(operator: UnaryOperator<UpdateRequest.Builder<T, T>>, documentId: String?, script: Script): Boolean {
        return api.update(operator, documentId, script)
    }

    open fun update(t: T): Boolean {
        return api.update(t)
    }

    open fun upsert(doc: T): Boolean {
        return api.upsert(doc)
    }

    open fun upsert(doc: T, script: Script): Boolean {
        return api.upsert(doc, script)
    }

    open fun update(operator: UnaryOperator<UpdateRequest.Builder<T, T>>, documentId: String?): Boolean {
        return api.update(operator, documentId)
    }

    open fun <E> update(operator: UnaryOperator<UpdateRequest.Builder<T, T>>, convert: Function<UpdateResponse<T>, E>): E {
        return api.update(operator, convert)
    }

    open fun updateByQuery(script: Script, vararg queries: Query): Boolean {
        return api.updateByQuery(script, *queries)
    }

    open fun updateByQuery(
        scriptOperator: Function<Script.Builder, ObjectBuilder<Script>>,
        queries: QueryBuilder<T>
    ): Boolean {
        return api.updateByQuery(scriptOperator, queries)
    }

    open fun updateByQuery(script: Script, queries: QueryBuilder<T>): Boolean {
        return api.updateByQuery(script, queries)
    }

    open fun updateByQuery(
        operator: UnaryOperator<UpdateByQueryRequest.Builder>, script: Script,
        queries: QueryBuilder<T>
    ): Boolean {
        return api.updateByQuery(operator, script, queries)
    }

    open fun <E> updateByQuery(
        operator: UnaryOperator<UpdateByQueryRequest.Builder>,
        script: Script, queries: QueryBuilder<T>, convert: Function<UpdateByQueryResponse, E>
    ): E {
        return api.updateByQuery(operator, script, queries, convert)
    }

    open fun bulk(vararg collection: T, convert: Function<T, BulkOperationBase.AbstractBuilder<*>>): BulkResponse {
        return api.bulk(collection.toList(), convert)
    }

    open fun bulk(collection: List<T>, convert: Function<T, BulkOperationBase.AbstractBuilder<*>>): BulkResponse {
        return api.bulk(collection, convert)
    }

    open fun bulk(operator: UnaryOperator<BulkRequest.Builder>, collection: Collection<T>, convert: Function<T, BulkOperationBase.AbstractBuilder<*>>): BulkResponse {
        return api.bulk(operator, collection, convert)
    }

    open fun bulk(vararg operations: BulkOperation): BulkResponse {
        return api.bulk(*operations)
    }

    open fun bulk(operations: List<BulkOperation>): BulkResponse {
        return api.bulk(operations)
    }

    open fun bulk(operator: UnaryOperator<BulkRequest.Builder>, operations: List<BulkOperation>): BulkResponse {
        return api.bulk(operator, operations)
    }

    open fun save(t: T) {
        api.save(t)
    }

    open fun saveBatch(collection: Collection<T>) {
        api.saveBatch(collection)
    }

    open fun saveBatch(operator: UnaryOperator<BulkRequest.Builder>, collection: Collection<T>) {
        api.saveBatch(operator, collection)
    }

    open fun <E> batch(collection: Collection<E>, function: Function<E, BulkOperation>): BulkResponse {
        return api.batch<E>(collection, function)
    }

    open fun <E> batch(
        operator: UnaryOperator<BulkRequest.Builder>, collection: Collection<E>,
        function: Function<E, BulkOperation>
    ): BulkResponse {
        return api.batch<E>(operator, collection, function)
    }

    open fun deleteByQuery(vararg queries: Query): Boolean {
        return api.deleteByQuery(*queries)
    }

    open fun deleteByQuery(queries: QueryBuilder<T>): Boolean {
        return api.deleteByQuery(queries)
    }

    open fun deleteByQuery(operator: UnaryOperator<DeleteByQueryRequest.Builder>, queries: QueryBuilder<T>): Boolean {
        return api.deleteByQuery(operator, queries)
    }

    open fun <E> deleteByQuery(operator: UnaryOperator<DeleteByQueryRequest.Builder>, queries: QueryBuilder<T>, convert: Function<DeleteByQueryResponse, E>): E {
        return api.deleteByQuery(operator, queries, convert)
    }

    open fun list(vararg queries: Query): List<T> {
        return api.list(*queries)
    }

    open fun list(queries: QueryBuilder<T>): List<T> {
        return api.list(queries)
    }

    open fun list(operator: UnaryOperator<SearchRequest.Builder>, vararg queries: Query): List<T> {
        return api.list(operator, *queries)
    }

    open fun list(operator: UnaryOperator<SearchRequest.Builder>, queries: QueryBuilder<T>): List<T> {
        return api.list(operator, queries)
    }

    open fun scroll(params: ScrollParams<String>, vararg queries: Query): ScrollResult<T, String> {
        return api.scroll(params, *queries)
    }

    open fun scroll(params: ScrollParams<String>, queries: QueryBuilder<T>): ScrollResult<T, String> {
        return api.scroll(params, queries)
    }

    open fun scroll(
        operator: UnaryOperator<SearchRequest.Builder>, params: ScrollParams<String>,
        queries: QueryBuilder<T>
    ): ScrollResult<T, String> {
        return api.scroll(operator, params, queries)
    }

    open fun scroll(operator: UnaryOperator<ScrollRequest.Builder>, scrollId: String): ScrollResult<T, String> {
        return api.scroll(operator, scrollId)
    }

    open fun clearScroll(scrollId: String) {
        api.clearScroll(scrollId)
    }

    open fun pageCursor(params: PaginationParams, vararg queries: Query): LimitCursor<T> {
        return api.pageCursor(params, *queries)
    }

    open fun pageCursor(params: PaginationParams, queries: QueryBuilder<T>): LimitCursor<T> {
        return api.pageCursor(params, queries)
    }

    open fun scrollCursor(params: ScrollParams<String>, vararg queries: Query): ScrollCursor<T, String> {
        return api.scrollCursor(params, *queries)
    }

    open fun scrollCursor(params: ScrollParams<String>, queries: QueryBuilder<T>): ScrollCursor<T, String> {
        return api.scrollCursor(params, queries)
    }

}
