package live.lingting.spring.jackson.configuration

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import live.lingting.framework.Sequence
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.spring.jackson.Customizer.SpringObjectMapperCustomizer
import live.lingting.spring.jackson.ObjectMapperCustomizer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * @author lingting 2024-02-02 13:51
 */
@ConditionalOnClass(ObjectMapper::class)
@AutoConfiguration(before = [JacksonAutoConfiguration::class])
open class SpringObjectMapperAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    open fun springObjectMapperCustomizer(modules: List<Module>) = SpringObjectMapperCustomizer(modules)

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnClass(Jackson2ObjectMapperBuilder::class)
    open fun objectMapperByWeb(builder: Jackson2ObjectMapperBuilder, customizers: List<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = builder.createXmlMapper(false).build<ObjectMapper>()
        val xmlMapper = builder.createXmlMapper(true).build<XmlMapper>()
        after(customizers, mapper, xmlMapper)
        return mapper
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnMissingClass("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
    open fun objectMapperByJackson(customizers: List<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = ObjectMapper()
        val xmlMapper = XmlMapper()
        after(customizers, mapper, xmlMapper)
        return mapper
    }

    fun after(customizers: List<ObjectMapperCustomizer>, mapper: ObjectMapper, xmlMapper: XmlMapper) {
        val asc = Sequence.asc(customizers)
        asc.forEach { it.apply(mapper) }
        JacksonUtils.mapper = mapper
        asc.forEach { it.apply(xmlMapper) }
        JacksonUtils.xmlMapper = xmlMapper
    }

}
