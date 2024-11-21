package live.lingting.spring.web.converter

import live.lingting.framework.util.EnumUtils.getCf
import live.lingting.framework.util.EnumUtils.getValue
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.stereotype.Component

/**
 * @author lingting 2022/9/28 12:17
 */
@Component
class EnumConverter : AbstractConverter<Enum<*>>() {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        // 来源类型判断
        return (String::class.java.isAssignableFrom(sourceType.getType())
                || Number::class.java.isAssignableFrom(sourceType.getType())) // 目标类型判断
                && targetType!!.getType().isEnum()
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        val set: MutableSet<ConvertiblePair> = HashSet<ConvertiblePair>()
        set.add(ConvertiblePair(String::class.java, Enum::class.java))
        set.add(ConvertiblePair(Number::class.java, Enum::class.java))
        return set
    }

    override fun convert(source: Any, sourceType: TypeDescriptor, targetType: TypeDescriptor): Enum<*> {
        var source = source
        if (source == null) {
            return null
        }

        val cf = getCf(targetType!!.getType())

        // 来源 转化成 目标类型
        if (cf == null || (convert(source, cf.valueType).also { source = it }) == null) {
            return null
        }
        for (o in targetType.getType().getEnumConstants()) {
            val value = getValue(o as Enum<*>)
            if (source == value) {
                return o as Enum<*>
            }
        }
        return null
    }
}
