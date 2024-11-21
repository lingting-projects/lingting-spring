package live.lingting.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 用于在objectMapper初始化之后设置位静态值
 *
 * @author lingting 2023-04-23 13:18
 */
fun interface ObjectMapperAfter {
    fun apply(mapper: ObjectMapper)
}
