package live.lingting.spring.spi

import live.lingting.framework.i18n.I18n
import live.lingting.framework.sensitive.SensitiveUtils
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * @author lingting 2024-05-21 11:34
 */
class SpiTest {
    @Test
    fun test() {
        val sensitive = SensitiveUtils.providers()
            .firstOrNull { SensitiveSpringProvider::class == it::class }
        assertNotNull(sensitive)
        val i18n = I18n.providers
            .firstOrNull { I18nSpringProvider::class == it::class }
        assertNotNull(i18n)
    }
}
