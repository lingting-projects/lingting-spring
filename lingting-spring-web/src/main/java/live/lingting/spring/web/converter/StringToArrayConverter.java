/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package live.lingting.spring.web.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

/**
 * Converts a comma-delimited String to an Array. Only matches if String.class can be
 * converted to the target array element type.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
@Component
public class StringToArrayConverter extends AbstractConverter<Object> {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), service);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String[] fields = toArray(source);

		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Assert.state(targetElementType != null, "No target element type");
		Object target = Array.newInstance(targetElementType.getType(), fields.length);
		for (int i = 0; i < fields.length; i++) {
			String sourceElement = fields[i];
			Object targetElement = service.convert(sourceElement.trim(), sourceType, targetElementType);
			Array.set(target, i, targetElement);
		}
		return target;
	}

}
