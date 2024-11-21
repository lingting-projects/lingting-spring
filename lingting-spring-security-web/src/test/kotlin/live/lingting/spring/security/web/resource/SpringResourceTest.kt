package live.lingting.spring.security.web.resource

import live.lingting.framework.security.resolver.SecurityTokenDefaultResolver
import live.lingting.framework.security.store.SecurityStore
import live.lingting.spring.util.SpringUtils
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-09-07 16:03
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringResourceTest {
    @Autowired
    private val resolver: SecurityTokenDefaultResolver? = null

    @Test
    fun test() {
        assertFalse(SpringUtils.hasBean(SecurityStore::class.java))
        assertNotNull(resolver)
    }
}
