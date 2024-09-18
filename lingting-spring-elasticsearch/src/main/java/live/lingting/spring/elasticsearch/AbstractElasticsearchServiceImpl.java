package live.lingting.spring.elasticsearch;

import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.util.ObjectBuilder;
import live.lingting.framework.api.LimitCursor;
import live.lingting.framework.api.PaginationParams;
import live.lingting.framework.api.PaginationResult;
import live.lingting.framework.api.ScrollCursor;
import live.lingting.framework.api.ScrollParams;
import live.lingting.framework.api.ScrollResult;
import live.lingting.framework.elasticsearch.ElasticsearchApi;
import live.lingting.framework.elasticsearch.builder.QueryBuilder;
import live.lingting.framework.elasticsearch.builder.ScriptBuilder;
import live.lingting.framework.function.ThrowingRunnable;
import live.lingting.framework.function.ThrowingSupplier;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static live.lingting.framework.elasticsearch.ElasticsearchUtils.getEntityClass;
import static live.lingting.framework.elasticsearch.ElasticsearchUtils.index;

/**
 * @author lingting 2024-03-08 16:43
 */
@SuppressWarnings("java:S1181")
public abstract class AbstractElasticsearchServiceImpl<T> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Getter
	protected final String index = index(getEntityClass(getClass()));

	@Getter
	protected final Class<T> cls = getEntityClass(getClass());

	@Getter
	@Setter
	protected ElasticsearchApi<T> api;

	public abstract String documentId(T t);

	// region extend

	public ScriptBuilder<T> script() {
		return ScriptBuilder.builder();
	}

	public QueryBuilder<T> query() {
		return QueryBuilder.builder();
	}

	public void tryIgnore(ThrowingRunnable runnable) {
		try {
			runnable.run();
		}
		catch (Throwable e) {
			log.warn("ignore error: {}", e.getMessage());
		}
	}

	public <R> R tryElse(ThrowingSupplier<R> supplier, R defaultValue) {
		return tryGet(supplier, () -> defaultValue);
	}

	public <R> R tryGet(ThrowingSupplier<R> supplier, Supplier<R> defaultValue) {
		return tryGet(supplier, e -> defaultValue.get());
	}

	public <R> R tryGet(ThrowingSupplier<R> supplier, Function<Throwable, R> defaultValue) {
		try {
			return supplier.get();
		}
		catch (Throwable e) {
			log.warn("try error: {}", e.getMessage());
			return defaultValue.apply(e);
		}
	}

	// endregion

	public void retry(ThrowingRunnable runnable) throws Exception {
		api.retry(runnable);
	}

	public <R> R retry(ThrowingSupplier<R> supplier) throws Exception {
		return api.retry(supplier);
	}

	public Query merge(Query... arrays) {
		return api.merge(arrays);
	}

	public Query merge(QueryBuilder<T> builder) {
		return api.merge(builder);
	}

	public T get(String id) throws IOException {
		return api.get(id);
	}

	public T getByQuery(Query... queries) throws IOException {
		return api.getByQuery(queries);
	}

	public T getByQuery(QueryBuilder<T> queries) throws IOException {
		return api.getByQuery(queries);
	}

	public T getByQuery(UnaryOperator<SearchRequest.Builder> operator, QueryBuilder<T> queries) throws IOException {
		return api.getByQuery(operator, queries);
	}

	public long count(Query... queries) throws IOException {
		return api.count(queries);
	}

	public long count(QueryBuilder<T> queries) throws IOException {
		return api.count(queries);
	}

	public HitsMetadata<T> search(Query... queries) throws IOException {
		return api.search(queries);
	}

	public HitsMetadata<T> search(QueryBuilder<T> queries) throws IOException {
		return api.search(queries);
	}

	public HitsMetadata<T> search(UnaryOperator<SearchRequest.Builder> operator, QueryBuilder<T> queries)
			throws IOException {
		return api.search(operator, queries);
	}

	public List<SortOptions> ofLimitSort(Collection<PaginationParams.Sort> sorts) {
		return api.ofLimitSort(sorts);
	}

	public PaginationResult<T> page(PaginationParams params) throws IOException {
		return api.page(params, QueryBuilder.builder());
	}

	public PaginationResult<T> page(PaginationParams params, QueryBuilder<T> queries) throws IOException {
		return api.page(params, queries);
	}

	public void aggs(BiConsumer<String, Aggregate> consumer, Map<String, Aggregation> aggregationMap,
			QueryBuilder<T> queries) throws IOException {
		api.aggs(consumer, aggregationMap, queries);
	}

	public void aggs(UnaryOperator<SearchRequest.Builder> operator, BiConsumer<String, Aggregate> consumer,
			Map<String, Aggregation> aggregationMap, QueryBuilder<T> queries) throws IOException {
		api.aggs(operator, consumer, aggregationMap, queries);
	}

	public void aggs(UnaryOperator<SearchRequest.Builder> operator, Consumer<SearchResponse<T>> consumer,
			Map<String, Aggregation> aggregationMap, QueryBuilder<T> queries) throws IOException {
		api.aggs(operator, consumer, aggregationMap, queries);
	}

	public boolean update(String documentId, Function<Script.Builder, ObjectBuilder<Script>> scriptOperator)
			throws IOException {
		return api.update(documentId, scriptOperator);
	}

	public boolean update(String documentId, Script script) throws IOException {
		return api.update(documentId, script);
	}

	public boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId, Script script)
			throws IOException {
		return api.update(operator, documentId, script);
	}

	public boolean update(T t) throws IOException {
		return api.update(t);
	}

	public boolean upsert(T doc) throws IOException {
		return api.upsert(doc);
	}

	public boolean upsert(T doc, Script script) throws IOException {
		return api.upsert(doc, script);
	}

	public boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId) throws IOException {
		return api.update(operator, documentId);
	}

	public boolean updateByQuery(Script script, Query... queries) throws IOException {
		return api.updateByQuery(script, queries);
	}

	public boolean updateByQuery(Function<Script.Builder, ObjectBuilder<Script>> scriptOperator,
			QueryBuilder<T> queries) throws IOException {
		return api.updateByQuery(scriptOperator, queries);
	}

	public boolean updateByQuery(Script script, QueryBuilder<T> queries) throws IOException {
		return api.updateByQuery(script, queries);
	}

	public boolean updateByQuery(UnaryOperator<UpdateByQueryRequest.Builder> operator, Script script,
			QueryBuilder<T> queries) throws IOException {
		return api.updateByQuery(operator, script, queries);
	}

	public BulkResponse bulk(BulkOperation... operations) throws IOException {
		return api.bulk(operations);
	}

	public BulkResponse bulk(List<BulkOperation> operations) throws IOException {
		return api.bulk(operations);
	}

	public BulkResponse bulk(UnaryOperator<BulkRequest.Builder> operator, List<BulkOperation> operations)
			throws IOException {
		return api.bulk(operator, operations);
	}

	public void save(T t) throws IOException {
		api.save(t);
	}

	public void saveBatch(Collection<T> collection) throws IOException {
		api.saveBatch(collection);
	}

	public void saveBatch(UnaryOperator<BulkRequest.Builder> operator, Collection<T> collection) throws IOException {
		api.saveBatch(operator, collection);
	}

	public <E> BulkResponse batch(Collection<E> collection, Function<E, BulkOperation> function) throws IOException {
		return api.batch(collection, function);
	}

	public <E> BulkResponse batch(UnaryOperator<BulkRequest.Builder> operator, Collection<E> collection,
			Function<E, BulkOperation> function) throws IOException {
		return api.batch(operator, collection, function);
	}

	public boolean deleteByQuery(Query... queries) throws IOException {
		return api.deleteByQuery(queries);
	}

	public boolean deleteByQuery(QueryBuilder<T> queries) throws IOException {
		return api.deleteByQuery(queries);
	}

	public boolean deleteByQuery(UnaryOperator<DeleteByQueryRequest.Builder> operator, QueryBuilder<T> queries)
			throws IOException {
		return api.deleteByQuery(operator, queries);
	}

	public List<T> list(Query... queries) throws IOException {
		return api.list(queries);
	}

	public List<T> list(QueryBuilder<T> queries) throws IOException {
		return api.list(queries);
	}

	public List<T> list(UnaryOperator<SearchRequest.Builder> operator, Query... queries) throws IOException {
		return api.list(operator, queries);
	}

	public List<T> list(UnaryOperator<SearchRequest.Builder> operator, QueryBuilder<T> queries) throws IOException {
		return api.list(operator, queries);
	}

	public ScrollResult<T, String> scroll(ScrollParams<String> params, Query... queries) throws IOException {
		return api.scroll(params, queries);
	}

	public ScrollResult<T, String> scroll(ScrollParams<String> params, QueryBuilder<T> queries) throws IOException {
		return api.scroll(params, queries);
	}

	public ScrollResult<T, String> scroll(UnaryOperator<SearchRequest.Builder> operator, ScrollParams<String> params,
			QueryBuilder<T> queries) throws IOException {
		return api.scroll(operator, params, queries);
	}

	public ScrollResult<T, String> scroll(UnaryOperator<ScrollRequest.Builder> operator, String scrollId)
			throws IOException {
		return api.scroll(operator, scrollId);
	}

	public void clearScroll(String scrollId) throws IOException {
		api.clearScroll(scrollId);
	}

	public LimitCursor<T> pageCursor(PaginationParams params, Query... queries) {
		return api.pageCursor(params, queries);
	}

	public LimitCursor<T> pageCursor(PaginationParams params, QueryBuilder<T> queries) {
		return api.pageCursor(params, queries);
	}

	public ScrollCursor<T, String> scrollCursor(ScrollParams<String> params, Query... queries) throws IOException {
		return api.scrollCursor(params, queries);
	}

	public ScrollCursor<T, String> scrollCursor(ScrollParams<String> params, QueryBuilder<T> queries)
			throws IOException {
		return api.scrollCursor(params, queries);
	}

}
