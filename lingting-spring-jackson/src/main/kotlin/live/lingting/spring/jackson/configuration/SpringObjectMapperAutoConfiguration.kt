package live.lingting.spring.jackson.configuration

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import live.lingting.framework.Sequence
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.spring.jackson.ObjectMapperCustomizer
import live.lingting.spring.jackson.customer.ObjectMapperConfigurator
import live.lingting.spring.jackson.customer.SpringObjectMapperConfigurator
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
    open fun objectMapperCustomer(
        serializerProviders: MutableList<DefaultSerializerProvider>,
        modules: MutableList<Module>,
        customizers: MutableList<ObjectMapperCustomizer>
    ): ObjectMapperConfigurator {
        // 升序排序
        Sequence.asc(customizers)
        return SpringObjectMapperConfigurator(serializerProviders, modules, customizers)
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnClass(Jackson2ObjectMapperBuilder::class)
    open fun objectMapperByWeb(builder: Jackson2ObjectMapperBuilder, configurator: ObjectMapperConfigurator): ObjectMapper {
        val mapper = builder.createXmlMapper(false).build<ObjectMapper>()
        val xmlMapper = builder.createXmlMapper(true).build<XmlMapper>()
        after(configurator, mapper, xmlMapper)
        return mapper
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnMissingClass("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
    open fun objectMapperByJackson(configurator: ObjectMapperConfigurator): ObjectMapper {
        val mapper = ObjectMapper()
        val xmlMapper = XmlMapper()
        after(configurator, mapper, xmlMapper)
        return mapper
    }

    fun after(configurator: ObjectMapperConfigurator, mapper: ObjectMapper, xmlMapper: XmlMapper) {
        configurator.apply(mapper)
        JacksonUtils.mapper = mapper
        configurator.apply(xmlMapper)
        JacksonUtils.xmlMapper = xmlMapper
    }

}
