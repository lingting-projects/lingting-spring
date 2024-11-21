package live.lingting.spring

import live.lingting.spring.util.EnvironmentUtils
import live.lingting.spring.util.SpringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-02-02 13:54
 */
@SpringBootTest
internal class SpringTest {
    @Test
    fun test() {
        assertTrue(EnvironmentUtils.isActiveProfiles("test"))
        val beans: MutableMap<String, SpringTestBean> = SpringUtils.getBeansOfType(SpringTestBean::class.java)
        assertEquals(1, beans.size)
        val bean = beans.values.iterator().next()
        assertEquals("bean", bean.value)
        assertTrue(bean.isStart)
    }
}
