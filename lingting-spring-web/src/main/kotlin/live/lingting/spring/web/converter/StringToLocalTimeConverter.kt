package live.lingting.spring.web.converter

import java.time.LocalTime
import live.lingting.framework.util.LocalTimeUtils
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair

/**
 * @author lingting 2022/9/28 12:17
 */
class StringToLocalTimeConverter : Converter<LocalTime> {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return String::class.java.isAssignableFrom(sourceType.type)
                && LocalTime::class.java.isAssignableFrom(targetType.type)
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, LocalTime::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): LocalTime {
        val string = source as String
        return LocalTimeUtils.parse(string)
    }
}
