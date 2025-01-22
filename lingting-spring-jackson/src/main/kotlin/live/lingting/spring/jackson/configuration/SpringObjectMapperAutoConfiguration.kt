package live.lingting.spring.jackson.configuration

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import live.lingting.spring.jackson.ObjectMapperCustomizer
import live.lingting.spring.jackson.customizer.SpringObjectMapperCustomizer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-02-02 13:51
 */
@ConditionalOnClass(ObjectMapper::class)
@AutoConfiguration(before = [JacksonAutoConfiguration::class], after = [SpringJacksonModuleAutoConfiguration::class])
open class SpringObjectMapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun springObjectMapperCustomizer(modules: List<Module>) = SpringObjectMapperCustomizer(modules)

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnMissingClass("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
    open fun objectMapperByJackson(customizers: List<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = ObjectMapper()
        val xmlMapper = XmlMapper()
        SpringObjectMapperWebAutoConfiguration.after(customizers, mapper, xmlMapper)
        return mapper
    }

}
