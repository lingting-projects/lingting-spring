package live.lingting.spring.i18n

import java.util.Locale
import live.lingting.framework.i18n.I18nSource
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException

/**
 * @author lingting 2024/12/4 13:10
 */
class MessageI18nSource(
    val message: MessageSource,
    override val locale: Locale
) : I18nSource {

    companion object {
        /**
         * 排序靠后, 优先用其他的
         */
        const val ORDER = 1000

    }

    override val sequence: Int = ORDER

    override fun find(key: String): String? {
        return try {
            message.getMessage(key, null, locale)
        } catch (_: NoSuchMessageException) {
            null
        }
    }
}
