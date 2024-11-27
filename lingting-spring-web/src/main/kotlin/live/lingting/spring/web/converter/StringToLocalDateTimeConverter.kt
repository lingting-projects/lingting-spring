package live.lingting.spring.web.converter

import java.time.LocalDateTime
import live.lingting.framework.util.LocalDateTimeUtils
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.stereotype.Component

/**
 * @author lingting 2022/9/28 12:17
 */
@Component
class StringToLocalDateTimeConverter : Converter<LocalDateTime> {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return String::class.java.isAssignableFrom(sourceType.type)
                && LocalDateTime::class.java.isAssignableFrom(targetType.type)
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, LocalDateTime::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): LocalDateTime {
        val string = source as String
        return LocalDateTimeUtils.parse(string)
    }
}
