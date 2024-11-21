package live.lingting.spring.elasticsearch;

import live.lingting.framework.api.PaginationParams;
import live.lingting.framework.elasticsearch.annotation.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author lingting 2024-03-08 17:32
 */
@SpringBootTest
class SpringElasticsearchTest {

	@Autowired
	private SwitchElasticsearchDataPermissionHandler handler;

	@Autowired
	private EntityServiceImpl service;

	@Test
	void test() {
		PaginationParams params = new PaginationParams();
		List<Entity> list = service.page(params).getRecords();
		assertFalse(list.isEmpty());
		handler.enable();
		Entity byQuery = service.getByQuery();
		assertNotNull(byQuery);
		assertEquals("Default", byQuery.getSpace().get("name"));
	}

	@Document(index = ".kibana_8.12.2_001")
	public static class Entity {

		private String id;

		private Map<String, Object> space;

		private Map<String, Object> config;

		private Object references;

		public String getId() {
			return this.id;
		}

		public Map<String, Object> getSpace() {
			return this.space;
		}

		public Map<String, Object> getConfig() {
			return this.config;
		}

		public Object getReferences() {
			return this.references;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setSpace(Map<String, Object> space) {
			this.space = space;
		}

		public void setConfig(Map<String, Object> config) {
			this.config = config;
		}

		public void setReferences(Object references) {
			this.references = references;
		}

	}

}
