package live.lingting.spring.elasticsearch

import live.lingting.framework.elasticsearch.annotation.Document
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-08 17:32
 */
@SpringBootTest
internal class SpringElasticsearchTest {
    @Autowired
    private val handler: SwitchElasticsearchDataPermissionHandler = null

    @Autowired
    private val service: EntityServiceImpl = null

    @Test
    fun test() {
        val params: PaginationParams = PaginationParams()
        val list: MutableList<Entity> = service!!.page(params).records
        Assertions.assertFalse(list.isEmpty())
        handler!!.enable()
        val byQuery = service.getByQuery()
        Assertions.assertNotNull(byQuery)
        assertEquals("Default", byQuery!!.space!!.get("name"))
    }

    @Document(index = ".kibana_8.12.2_001")
    class Entity {
        var id: String = null

        var space: MutableMap<String, Any> = null

        var config: MutableMap<String, Any> = null

        var references: Any = null
    }
}
