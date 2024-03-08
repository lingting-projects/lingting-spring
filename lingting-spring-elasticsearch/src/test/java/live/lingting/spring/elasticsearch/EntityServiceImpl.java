package live.lingting.spring.elasticsearch;

import org.springframework.stereotype.Component;

/**
 * @author lingting 2024-03-08 18:08
 */
@Component
public class EntityServiceImpl extends AbstractElasticsearchServiceImpl<SpringElasticsearchTest.Entity> {

	@Override
	public String documentId(SpringElasticsearchTest.Entity entity) {
		return entity.getId();
	}

}
