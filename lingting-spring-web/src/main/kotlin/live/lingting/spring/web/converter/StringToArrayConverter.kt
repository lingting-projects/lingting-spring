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
package live.lingting.spring.web.converter

import java.lang.reflect.Array
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.util.Assert

/**
 * Converts a comma-delimited String to an Array. Only matches if String.class can be
 * converted to the target array element type.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
@Component
class StringToArrayConverter : AbstractConverter<Any>() {
    override fun getConvertibleTypes(): MutableSet<ConvertiblePair> {
        return mutableSetOf<ConvertiblePair>(ConvertiblePair(String::class.java, kotlin.Array::class.java))
    }

    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), service!!)
    }

    @Nullable
    override fun convert(@Nullable source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any {
        val fields = toArray(source)

        val targetElementType = targetType.getElementTypeDescriptor()
        Assert.state(targetElementType != null, "No target element type")
        val target = Array.newInstance(targetElementType!!.getType(), fields.size)
        for (i in fields.indices) {
            val sourceElement = fields[i]
            val targetElement = service!!.convert(sourceElement.trim { it <= ' ' }, sourceType, targetElementType)
            Array.set(target, i, targetElement)
        }
        return target
    }
}
