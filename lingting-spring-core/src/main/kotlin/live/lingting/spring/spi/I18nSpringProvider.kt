package live.lingting.spring.spi

import live.lingting.framework.i18n.I18nProvider
import live.lingting.framework.i18n.I18nSource
import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024/12/4 11:50
 */
class I18nSpringProvider : SpringSpiLoader<I18nProvider>, I18nProvider {

    val providers by lazy { all() }

    override val cls: Class<I18nProvider> = I18nProvider::class.java

    override fun load(): Collection<I18nSource> {
        if (SpringUtils.context == null) {
            return emptyList()
        }
        val values = HashSet<I18nSource>()
        for (provider in providers) {
            val sources = provider.load()
            values.addAll(sources)
        }
        val beans = SpringUtils.getBeansOfType(I18nSource::class).values
        values.addAll(beans)
        return values
    }
}
