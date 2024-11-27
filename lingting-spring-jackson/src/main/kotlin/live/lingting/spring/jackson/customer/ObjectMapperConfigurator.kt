package live.lingting.spring.jackson.customer

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author lingting 2024/11/27 15:18
 */
fun interface ObjectMapperConfigurator {

    fun apply(mapper: ObjectMapper)

}
