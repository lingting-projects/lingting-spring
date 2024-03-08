package live.lingting.spring.elasticsearch;

import live.lingting.framework.api.PaginationParams;
import live.lingting.framework.elasticsearch.annotation.Document;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

	@SneakyThrows
	@Test
	void test() {
		List<Entity> list = service.page(new PaginationParams(1, 10, null)).getRecords();
		assertFalse(list.isEmpty());
		handler.enable();
		Entity byQuery = service.getByQuery();
		assertNotNull(byQuery);
		assertEquals("Default", byQuery.getSpace().get("name"));
	}

	@Data
	@Document(index = ".kibana_8.12.2_001")
	public static class Entity {

		private String id;

		private Map<String, Object> space;

		private Map<String, Object> config;

		private Object references;

	}

}
