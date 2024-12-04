package live.lingting.spring.spi

import live.lingting.framework.sensitive.Sensitive
import live.lingting.framework.sensitive.SensitiveProvider
import live.lingting.framework.sensitive.SensitiveSerializer
import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024-05-21 11:30
 */
class SensitiveSpringProvider : SpringSpiLoader<SensitiveProvider>, SensitiveProvider {

    val providers by lazy { all() }

    override val cls: Class<SensitiveProvider> = SensitiveProvider::class.java

    override fun find(sensitive: Sensitive): SensitiveSerializer? {
        if (SpringUtils.context == null) {
            return null
        }

        for (provider in providers) {
            val serializer = provider.find(sensitive)
            if (serializer != null) {
                return serializer
            }
        }

        if (SpringUtils.hasBean(sensitive.value.java)) {
            val beans = SpringUtils.getBeansOfType(sensitive.value).values
            return beans.firstOrNull()
        }

        return null
    }

    override val sequence: Int = 100
}
