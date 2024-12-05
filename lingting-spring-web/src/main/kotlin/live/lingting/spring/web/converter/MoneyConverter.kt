package live.lingting.spring.web.converter

import live.lingting.framework.money.Money
import live.lingting.framework.util.StringUtils.hasText
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair

/**
 * @author lingting 2023-05-07 18:38
 */
class MoneyConverter : ConditionalGenericConverter {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return Any::class.java.isAssignableFrom(sourceType.type)
                && Money::class.java.isAssignableFrom(targetType.type)
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(Any::class.java, Money::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        if (source == null || !hasText(source.toString())) {
            return null
        }
        return Money.of(source.toString())
    }
}
