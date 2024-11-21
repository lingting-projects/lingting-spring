package live.lingting.spring.web.converter

import org.springframework.core.CollectionFactory
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component

/**
 * copy by StringToCollectionConverter
 *
 * @author lingting 2022/9/28 10:33
 */
@Component
class StringToCollectionConverter : AbstractConverter<MutableCollection<Any>>() {
    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, MutableCollection::class.java))
    }

    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        val targetDescriptor = targetType.getElementTypeDescriptor()
        if (targetDescriptor == null) {
            return true
        }

        return service.canConvert(sourceType, targetDescriptor)
    }

    @Nullable
    override fun convert(@Nullable source: Any, sourceType: TypeDescriptor, targetType: TypeDescriptor): MutableCollection<Any> {
        val fields = toArray(source)

        val elementDesc = targetType.getElementTypeDescriptor()
        val target = CollectionFactory.createCollection<Any>(
            targetType.getType(),
            (if (elementDesc != null) elementDesc.getType() else null), fields.size
        )

        if (elementDesc == null) {
            for (field in fields) {
                target.add(field.trim { it <= ' ' })
            }
        } else {
            for (field in fields) {
                val targetElement = service.convert(field.trim { it <= ' ' }, sourceType, elementDesc)
                target.add(targetElement)
            }
        }
        return target
    }
}
