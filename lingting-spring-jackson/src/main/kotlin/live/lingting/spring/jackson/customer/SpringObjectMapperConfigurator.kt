package live.lingting.spring.jackson.customer

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import live.lingting.spring.jackson.ObjectMapperCustomizer

/**
 * @author lingting 2024/11/27 15:19
 */
class SpringObjectMapperConfigurator(
    val serializerProviders: List<DefaultSerializerProvider>,
    val modules: List<Module>,
    val customizers: List<ObjectMapperCustomizer>,
) : ObjectMapperConfigurator {
    override fun apply(mapper: ObjectMapper) {
        // 序列化时忽略未知属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        // 单值元素可以被设置成 array, 防止处理 ["a"] 为 List<String> 时报错
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        // 空对象不报错
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        // 有特殊需要转义字符, 不报错
        mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())

        for (serializerProvider in serializerProviders) {
            mapper.setSerializerProvider(serializerProvider)
        }

        for (module in modules) {
            mapper.registerModule(module)
        }

        for (mapperCustomizer in customizers) {
            mapperCustomizer.apply(mapper)
        }
    }
}
