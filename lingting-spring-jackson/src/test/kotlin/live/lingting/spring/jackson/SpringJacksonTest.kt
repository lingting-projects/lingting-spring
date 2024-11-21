package live.lingting.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import live.lingting.framework.jackson.JacksonUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-02-02 15:56
 */
@SpringBootTest
class SpringJacksonTest {
    @Autowired
    private val mapper: ObjectMapper? = null

    @Autowired
    private val customizer: TestObjectMapperCustomizer? = null

    @Autowired
    private val after: TestObjectMapperAfter? = null

    @Test
    fun test() {
        assertEquals(JacksonUtils.mapper, mapper)
        assertEquals(customizer!!.objectMapper, mapper)
        assertTrue(after!!.isAfter)
    }
}
