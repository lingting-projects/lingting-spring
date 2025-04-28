package live.lingting.spring

import live.lingting.framework.concurrent.Await
import live.lingting.framework.thread.AbstractTimer
import live.lingting.framework.util.DurationUtils.seconds
import live.lingting.spring.util.EnvironmentUtils
import live.lingting.spring.util.SpringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-02-02 13:54
 */
@SpringBootTest
class SpringTest {

    @Test
    fun test() {
        assertTrue(EnvironmentUtils.isActiveProfiles("test"))
        val beans = SpringUtils.getBeansOfType(SpringTestBean::class.java)
        assertEquals(1, beans.size)
        val bean = beans.values.iterator().next()
        assertEquals("bean", bean.value)
        assertTrue(bean.isStart)
        val timer = SpringUtils.getBean(Timer::class)
        Await.wait(1.seconds)
        assertEquals(1, timer.count)
    }
}


@Component
class Timer : AbstractTimer() {
    var count = 0

    override fun process() {
        count += 1
    }

}
