package live.lingting.spring.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import live.lingting.framework.elasticsearch.composer.QueryComposer;
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2024-03-08 18:05
 */
@Component
public class SpaceDataScope implements ElasticsearchDataScope {

	@Override
	public String getResource() {
		return "null";
	}

	@Override
	public boolean includes(String index) {
		return true;
	}

	@Override
	public Query invoke(String index) {
		return QueryComposer.term("space.name", "default");
	}

}
