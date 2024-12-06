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

        /**
         * 默认值, 用于避免 message 回滚到系统语言去, 然后导致默认值丢失
         */
        @JvmStatic
        var defaultNull = "N_M"

    }

    override val sequence: Int = ORDER

    override fun find(key: String): String? {
        return try {
            // 避免被修改导致不一致
            val default = defaultNull
            val text = message.getMessage(key, null, default, locale)
            if (text == default) {
                null
            } else {
                text
            }
        } catch (_: NoSuchMessageException) {
            null
        }
    }
}
