package live.lingting.spring.spi

import live.lingting.framework.sensitive.Sensitive
import live.lingting.framework.sensitive.SensitiveProvider
import live.lingting.framework.sensitive.SensitiveSerializer
import live.lingting.spring.util.SpringUtils
import org.springframework.beans.BeansException

/**
 * @author lingting 2024-05-21 11:30
 */
class SensitiveSpringProvider : SensitiveProvider {
    override fun find(sensitive: Sensitive): SensitiveSerializer? {
        return try {
            SpringUtils.getBean(sensitive.value.java)
        } catch (e: BeansException) {
            null
        }
    }

    override val sequence: Int = 100
}
