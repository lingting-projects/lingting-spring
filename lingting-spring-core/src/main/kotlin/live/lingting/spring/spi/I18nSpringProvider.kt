package live.lingting.spring.spi

import java.util.Locale
import java.util.Objects
import live.lingting.framework.i18n.I18nProvider
import live.lingting.framework.i18n.I18nSource
import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024/12/4 11:50
 */
class I18nSpringProvider : SpringSpiLoader<I18nProvider>, I18nProvider {

    override val cls: Class<I18nProvider> = I18nProvider::class.java

    val providers by lazy { all() }

    val beans by lazy { SpringUtils.getBeansOfType(I18nSource::class).values }

    override fun find(locale: Locale): Collection<I18nSource>? {
        val list = ArrayList<I18nSource>()

        providers.forEach {
            val sources = it.find(locale)
            if (sources != null) {
                list.addAll(sources)
            }
        }

        beans.forEach {
            if (Objects.equals(it.locale, locale)) {
                list.add(it)
            }
        }

        return list
    }
}
