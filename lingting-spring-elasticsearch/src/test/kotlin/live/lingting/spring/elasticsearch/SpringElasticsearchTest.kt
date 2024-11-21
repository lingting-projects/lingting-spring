package live.lingting.spring.elasticsearch

import live.lingting.framework.api.PaginationParams
import live.lingting.framework.elasticsearch.annotation.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-08 17:32
 */
@SpringBootTest
class SpringElasticsearchTest {
    @Autowired
    private val handler: SwitchElasticsearchDataPermissionHandler? = null

    @Autowired
    private val service: EntityServiceImpl? = null

    @Test
    fun test() {
        val params = PaginationParams()
        val list = service!!.page(params).records
        assertFalse(list.isEmpty())
        handler!!.enable()
        val byQuery = service.getByQuery()
        assertNotNull(byQuery)
        assertEquals("Default", byQuery!!.space["name"])
    }

    @Document(index = ".kibana_8.12.2_001")
    class Entity {
        var id: String = ""

        var space: Map<String, Any> = emptyMap<String, Any>()

        var config: Map<String, Any> = emptyMap<String, Any>()

        var references: Any? = null
    }
}
