package live.lingting.spring.web.converter

import live.lingting.framework.money.Money
import live.lingting.framework.util.StringUtils.hasText
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.stereotype.Component

/**
 * @author lingting 2023-05-07 18:38
 */
@Component
class MoneyConverter : ConditionalGenericConverter {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return Any::class.java.isAssignableFrom(sourceType.getType())
                && Money::class.java.isAssignableFrom(targetType!!.getType())
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(Any::class.java, Money::class.java))
    }

    override fun convert(source: Any, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any {
        if (source == null || !hasText(source.toString())) {
            return null
        }
        return of.of(source.toString())
    }
}
