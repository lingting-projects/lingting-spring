/*
 * Copyright 2002-2019 the original author or authors.
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
package live.lingting.spring.web.converter

import java.util.function.Supplier
import org.springframework.core.convert.ConversionFailedException
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.lang.Nullable
import org.springframework.util.Assert
import org.springframework.util.ClassUtils

/**
 * Internal utilities for the conversion package.
 *
 * @author Keith Donald
 * @author Stephane Nicoll
 * @since 3.0
 */
class ConversionUtils private constructor() {
    init {
        throw UnsupportedOperationException("This is a utility class and cannot be instantiated")
    }

    companion object {
        @Nullable
        fun invokeConverter(
            converter: GenericConverter, @Nullable source: Any, sourceType: TypeDescriptor,
            targetType: TypeDescriptor
        ): Any {
            try {
                return converter.convert(source, sourceType, targetType)
            } catch (ex: ConversionFailedException) {
                throw ex
            } catch (ex: Throwable) {
                throw ConversionFailedException(sourceType, targetType, source, ex)
            }
        }

        fun canConvertElements(
            @Nullable sourceElementType: TypeDescriptor,
            @Nullable targetElementType: TypeDescriptor, conversionService: ConversionService
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

        fun getEnumType(targetType: Class<*>): Class<*> {
            var enumType: Class<*> = targetType
            while (enumType != null && !enumType.isEnum()) {
                enumType = enumType.getSuperclass()
            }
            Assert.notNull(enumType, Supplier { "The target type " + targetType.getName() + " does not refer to an enum" })
            return enumType!!
        }
    }
}
