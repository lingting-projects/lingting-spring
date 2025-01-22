package live.lingting.spring.jackson.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import live.lingting.framework.Sequence
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.spring.jackson.ObjectMapperCustomizer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * @author lingting 2024-02-02 13:51
 */
@AutoConfiguration(before = [JacksonAutoConfiguration::class])
@ConditionalOnClass(ObjectMapper::class, Jackson2ObjectMapperBuilder::class)
open class SpringObjectMapperWebAutoConfiguration {

    companion object {

        @JvmStatic
        fun after(customizers: List<ObjectMapperCustomizer>, mapper: ObjectMapper, xmlMapper: XmlMapper) {
            val asc = Sequence.asc(customizers)
            asc.forEach { it.apply(mapper) }
            JacksonUtils.mapper = mapper
            asc.forEach { it.apply(xmlMapper) }
            JacksonUtils.xmlMapper = xmlMapper
        }

    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    open fun objectMapperByWeb(builder: Jackson2ObjectMapperBuilder, customizers: List<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = builder.createXmlMapper(false).build<ObjectMapper>()
        val xmlMapper = builder.createXmlMapper(true).build<XmlMapper>()
        after(customizers, mapper, xmlMapper)
        return mapper
    }

}
