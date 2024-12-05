package live.lingting.spring.web.converter

import java.lang.reflect.Array
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.lang.Nullable
import org.springframework.util.Assert

/**
 * Converts a comma-delimited String to an Array. Only matches if String.class can be
 * converted to the target array element type.
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
class StringToArrayConverter : AbstractConverter<Any>() {
    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, kotlin.Array::class.java))
    }

    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), service)
    }

    @Nullable
    override fun convert(@Nullable source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any {
        val fields = toArray(source)

        val targetElementType = targetType.getElementTypeDescriptor()
        Assert.state(targetElementType != null, "No target element type")
        val target = Array.newInstance(targetElementType!!.getType(), fields.size)
        for (i in fields.indices) {
            val sourceElement = fields[i]
            val targetElement = service.convert(sourceElement.trim(), sourceType, targetElementType)
            Array.set(target, i, targetElement)
        }
        return target
    }
}
