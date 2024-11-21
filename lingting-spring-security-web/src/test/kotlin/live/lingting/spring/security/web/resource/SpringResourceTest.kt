package live.lingting.spring.security.web.resource

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

/**
 * @author lingting 2024-09-07 16:03
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringResourceTest {
    @Autowired
    private val resolver: SecurityTokenDefaultResolver = null

    @Test
    fun test() {
        assertFalse(SpringUtils.hasBean(SecurityStore::class.java))
        Assertions.assertNotNull(resolver)
    }
}
