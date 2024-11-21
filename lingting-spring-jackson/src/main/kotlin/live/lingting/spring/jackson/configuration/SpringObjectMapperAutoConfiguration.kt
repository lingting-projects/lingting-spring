package live.lingting.spring.jackson.configuration

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.spring.jackson.ObjectMapperAfter
import live.lingting.spring.jackson.ObjectMapperCustomizer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.AnnotationAwareOrderComparator

/**
 * @author lingting 2024-02-02 13:51
 */
@ConditionalOnClass(ObjectMapper::class)
@AutoConfiguration(before = [JacksonAutoConfiguration::class])
open class SpringObjectMapperAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    open fun objectMapper(
        serializerProviders: MutableList<DefaultSerializerProvider>, modules: MutableList<Module>,
        customizers: MutableList<ObjectMapperCustomizer>, afters: MutableList<ObjectMapperAfter>
    ): ObjectMapper {
        customizers.sortWith(AnnotationAwareOrderComparator.INSTANCE)
        afters.sortWith(AnnotationAwareOrderComparator.INSTANCE)

        var mapper = ObjectMapper()

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
            mapper = mapperCustomizer.apply(mapper)
        }

        JacksonUtils.mapper = mapper
        for (after in afters) {
            after.apply(mapper)
        }
        return mapper
    }
}
