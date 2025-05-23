package live.lingting.spring.web.converter

import java.time.LocalDate
import live.lingting.framework.util.LocalDateUtils
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair

/**
 * @author lingting 2022/9/28 12:17
 */
class StringToLocalDateConverter : Converter<LocalDate> {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return String::class.java.isAssignableFrom(sourceType.type)
                && LocalDate::class.java.isAssignableFrom(targetType.type)
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, LocalDate::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): LocalDate {
        val string = source as String
        return LocalDateUtils.parse(string)
    }

}
