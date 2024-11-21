package live.lingting.spring.spi

import live.lingting.framework.sensitive.SensitiveUtils.providers
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author lingting 2024-05-21 11:34
 */
internal class SpiTest {
    @Test
    fun test() {
        val optional = providers()
            .stream()
            .filter { p -> SensitiveSpringProvider::class.java.isAssignableFrom(p!!.javaClass) }
            .findAny()
        assertTrue(optional.isPresent)
    }
}
