package live.lingting.spring.web.converter;

import live.lingting.framework.util.LocalDateTimeUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
@Component
public class StringToLocalTimeConverter implements Converter<LocalTime> {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return String.class.isAssignableFrom(sourceType.getType())
				&& LocalTime.class.isAssignableFrom(targetType.getType());
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, LocalTime.class));
	}

	@Override
	public LocalTime convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String string = (String) source;
		return LocalDateTimeUtils.parseTime(string);
	}

}
