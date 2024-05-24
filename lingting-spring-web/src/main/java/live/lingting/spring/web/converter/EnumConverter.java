package live.lingting.spring.web.converter;

import live.lingting.framework.reflect.ClassField;
import live.lingting.framework.util.EnumUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
@Component
@SuppressWarnings("java:S3740")
public class EnumConverter extends AbstractConverter<Enum> {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		// 来源类型判断
		return (String.class.isAssignableFrom(sourceType.getType())
				|| Number.class.isAssignableFrom(sourceType.getType()))
				// 目标类型判断
				&& targetType.getType().isEnum();
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> set = new HashSet<>();
		set.add(new ConvertiblePair(String.class, Enum.class));
		set.add(new ConvertiblePair(Number.class, Enum.class));
		return set;
	}

	@Override
	public Enum convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		ClassField cf = EnumUtils.getCf(targetType.getType());

		// 来源 转化成 目标类型
		if (cf == null || (source = convert(source, cf.getValueType())) == null) {
			return null;
		}
		for (Object o : targetType.getType().getEnumConstants()) {
			Object value = EnumUtils.getValue((Enum<?>) o);
			if (Objects.equals(source, value)) {
				return (Enum) o;
			}
		}
		return null;
	}

}
