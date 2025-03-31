package live.lingting.spring.web.converter

import org.springframework.core.convert.ConversionFailedException
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import java.util.function.Supplier

/**
 * Internal utilities for the conversion package.
 * @author Keith Donald
 * @author Stephane Nicoll
 * @since 3.0
 */
object ConversionUtils {

    @JvmStatic
    fun invokeConverter(
        converter: GenericConverter, source: Any?, sourceType: TypeDescriptor,
        targetType: TypeDescriptor
    ): Any? {
        try {
            return converter.convert(source, sourceType, targetType)
        } catch (ex: ConversionFailedException) {
            throw ex
        } catch (ex: Throwable) {
            throw ConversionFailedException(sourceType, targetType, source, ex)
        }
    }

    @JvmStatic
    fun canConvertElements(
        sourceElementType: TypeDescriptor?,
        targetElementType: TypeDescriptor?,
        conversionService: ConversionService
    ): Boolean {
        if (targetElementType == null) {
            // yes
            return true
        }
        if (sourceElementType == null) {
            // maybe
            return true
        }
        if (conversionService.canConvert(sourceElementType, targetElementType)) {
            // yes
            return true
        }
        if (ClassUtils.isAssignable(sourceElementType.getType(), targetElementType.getType())) {
            // maybe
            return true
        }
        // no
        return false
    }

    @JvmStatic
    fun getEnumType(targetType: Class<*>?): Class<*> {
        var enumType = targetType
        while (enumType != null && !enumType.isEnum) {
            enumType = enumType.getSuperclass()
        }
        Assert.notNull(enumType, Supplier { "The target type " + targetType?.getName() + " does not refer to an enum" })
        return enumType!!
    }

}
