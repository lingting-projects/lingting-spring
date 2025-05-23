package live.lingting.spring.elasticsearch

import live.lingting.framework.api.PaginationParams
import live.lingting.framework.elasticsearch.annotation.Index
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-08 17:32
 */
@SpringBootTest
class SpringElasticsearchTest {
    @Autowired
    private val scope: SpaceDataScope? = null

    @Autowired
    private val service: EntityServiceImpl? = null

    @Test
    fun test() {
        val params = PaginationParams()
        val list = service!!.page(params).records
        assertFalse(list.isEmpty())
        assertNotNull(scope)
        assertFalse(scope!!.ignore(null))
        val byQuery = service.get()
        assertNotNull(byQuery)
        assertTrue(listOf("default", "默认").any { it == byQuery?.space["name"]?.toString()?.lowercase() })
    }

    @Index(index = ".kibana_8.12.2_001")
    class Entity {
        var id: String = ""

        var space: Map<String, Any> = emptyMap<String, Any>()

        var config: Map<String, Any> = emptyMap<String, Any>()

        var references: Any? = null
    }
}
