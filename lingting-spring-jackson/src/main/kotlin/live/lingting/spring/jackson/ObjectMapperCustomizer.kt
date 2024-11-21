package live.lingting.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 用于自定义objectMapper
 *
 * @author lingting 2023-04-23 13:18
 */
fun interface ObjectMapperCustomizer {
    fun apply(mapper: ObjectMapper): ObjectMapper
}
