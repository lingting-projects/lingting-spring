package live.lingting.spring.web.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2022/9/28 11:14
 */
public interface Converter<T> extends ConditionalGenericConverter {

	default String[] toArray(Object source) {
		if (source == null) {
			return new String[0];
		}
		String string = ((String) source).trim();

		if (string.startsWith("[") && string.endsWith("]")) {
			string = string.substring(1, string.length() - 1);
		}
		return StringUtils.commaDelimitedListToStringArray(string);
	}

	@Override
	T convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

}
