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
import java.util.function.UnaryOperator;

import static live.lingting.framework.elasticsearch.ElasticsearchUtils.getEntityClass;
import static live.lingting.framework.elasticsearch.ElasticsearchUtils.index;

/**
 * @author lingting 2024-03-08 16:43
 */
public abstract class AbstractElasticsearchServiceImpl<T> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Getter
	@Setter
	protected ElasticsearchApi<T> api;

	@Getter
	protected final String index = index(getEntityClass(getClass()));

	@Getter
	protected final Class<T> cls = getEntityClass(getClass());

	public abstract String documentId(T t);

	public void retry(ThrowingRunnable runnable) throws Exception {
		api.retry(runnable);
	}

	public <R> R retry(ThrowingSupplier<R> supplier) throws Exception {
		return api.retry(supplier);
	}

	public Query merge(Query... arrays) {
		return api.merge(arrays);
	}

	protected T getByQuery(Query... queries) throws IOException {
		return api.getByQuery(queries);
	}

	protected T getByQuery(UnaryOperator<SearchRequest.Builder> operator, Query... queries) throws IOException {
		return api.getByQuery(operator, queries);
	}

	protected long count(Query... queries) throws IOException {
		return api.count(queries);
	}

	protected HitsMetadata<T> search(Query... queries) throws IOException {
		return api.search(queries);
	}

	protected HitsMetadata<T> search(UnaryOperator<SearchRequest.Builder> operator, Query... queries)
			throws IOException {
		return api.search(operator, queries);
	}

	protected List<SortOptions> ofLimitSort(Collection<PaginationParams.Sort> sorts) {
		return api.ofLimitSort(sorts);
	}

	protected PaginationResult<T> page(PaginationParams params, Query... queries) throws IOException {
		return api.page(params, queries);
	}

	protected void aggs(BiConsumer<String, Aggregate> consumer, Map<String, Aggregation> aggregationMap,
			Query... queries) throws IOException {
		api.aggs(consumer, aggregationMap, queries);
	}

	protected void aggs(UnaryOperator<SearchRequest.Builder> operator, BiConsumer<String, Aggregate> consumer,
			Map<String, Aggregation> aggregationMap, Query... queries) throws IOException {
		api.aggs(operator, consumer, aggregationMap, queries);
	}

	protected void aggs(UnaryOperator<SearchRequest.Builder> operator, Consumer<SearchResponse<T>> consumer,
			Map<String, Aggregation> aggregationMap, Query... queries) throws IOException {
		api.aggs(operator, consumer, aggregationMap, queries);
	}

	protected boolean update(String documentId, Function<Script.Builder, ObjectBuilder<Script>> scriptOperator)
			throws IOException {
		return api.update(documentId, scriptOperator);
	}

	protected boolean update(String documentId, Script script) throws IOException {
		return api.update(documentId, script);
	}

	protected boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId, Script script)
			throws IOException {
		return api.update(operator, documentId, script);
	}

	protected boolean update(T t) throws IOException {
		return api.update(t);
	}

	protected boolean upsert(T doc) throws IOException {
		return api.upsert(doc);
	}

	protected boolean upsert(T doc, Script script) throws IOException {
		return api.upsert(doc, script);
	}

	protected boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId)
			throws IOException {
		return api.update(operator, documentId);
	}

	protected boolean updateByQuery(Function<Script.Builder, ObjectBuilder<Script>> scriptOperator, Query... queries)
			throws IOException {
		return api.updateByQuery(scriptOperator, queries);
	}

	protected boolean updateByQuery(Script script, Query... queries) throws IOException {
		return api.updateByQuery(script, queries);
	}

	protected boolean updateByQuery(UnaryOperator<UpdateByQueryRequest.Builder> operator, Script script,
			Query... queries) throws IOException {
		return api.updateByQuery(operator, script, queries);
	}

	protected BulkResponse bulk(BulkOperation... operations) throws IOException {
		return api.bulk(operations);
	}

	protected BulkResponse bulk(List<BulkOperation> operations) throws IOException {
		return api.bulk(operations);
	}

	protected BulkResponse bulk(UnaryOperator<BulkRequest.Builder> operator, List<BulkOperation> operations)
			throws IOException {
		return api.bulk(operator, operations);
	}

	protected void save(T t) throws IOException {
		api.save(t);
	}

	protected void saveBatch(Collection<T> collection) throws IOException {
		api.saveBatch(collection);
	}

	protected void saveBatch(UnaryOperator<BulkRequest.Builder> operator, Collection<T> collection) throws IOException {
		api.saveBatch(operator, collection);
	}

	protected boolean deleteByQuery(Query... queries) throws IOException {
		return api.deleteByQuery(queries);
	}

	protected boolean deleteByQuery(UnaryOperator<DeleteByQueryRequest.Builder> operator, Query... queries)
			throws IOException {
		return api.deleteByQuery(operator, queries);
	}

	protected List<T> list(Query... queries) throws IOException {
		return api.list(queries);
	}

	protected List<T> list(UnaryOperator<SearchRequest.Builder> operator, Query... queries) throws IOException {
		return api.list(operator, queries);
	}

	protected ScrollResult<T, String> scroll(ScrollParams<String> params, Query... queries) throws IOException {
		return api.scroll(params, queries);
	}

	protected ScrollResult<T, String> scroll(UnaryOperator<SearchRequest.Builder> operator, ScrollParams<String> params,
			Query... queries) throws IOException {
		return api.scroll(operator, params, queries);
	}

	protected ScrollResult<T, String> scroll(UnaryOperator<ScrollRequest.Builder> operator, String scrollId)
			throws IOException {
		return api.scroll(operator, scrollId);
	}

	protected void clearScroll(String scrollId) throws IOException {
		api.clearScroll(scrollId);
	}

	protected LimitCursor<T> pageCursor(PaginationParams params, Query... queries) {
		return api.pageCursor(params, queries);
	}

	protected ScrollCursor<T, String> scrollCursor(ScrollParams<String> params, Query... queries) throws IOException {
		return api.scrollCursor(params, queries);
	}

}
