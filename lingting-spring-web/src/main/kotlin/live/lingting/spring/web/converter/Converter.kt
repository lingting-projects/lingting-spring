package live.lingting.spring.web.converter

import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.util.StringUtils

/**
 * @author lingting 2022/9/28 11:14
 */
interface Converter<T> : ConditionalGenericConverter {
    fun toArray(source: Any): Array<String> {
        if (source == null) {
            return arrayOfNulls<String>(0)
        }
        var string = (source as String).trim { it <= ' ' }

        if (string.startsWith("[") && string.endsWith("]")) {
            string = string.substring(1, string.length - 1)
        }
        return StringUtils.commaDelimitedListToStringArray(string)
    }

    override fun convert(source: Any, sourceType: TypeDescriptor, targetType: TypeDescriptor): T
}
