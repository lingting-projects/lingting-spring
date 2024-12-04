package live.lingting.spring.i18n

import java.util.Locale
import live.lingting.framework.i18n.I18nProvider
import live.lingting.framework.i18n.I18nSource
import org.springframework.context.MessageSource

/**
 * @author lingting 2024/12/4 13:06
 */
open class MessageI18nProvider(val message: MessageSource) : I18nProvider {

    override fun find(locale: Locale): Collection<I18nSource>? {
        return listOf(MessageI18nSource(message, locale))
    }

}
