package live.lingting.spring.jackson.customer

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author lingting 2024/11/27 15:18
 */
interface ObjectMapperConfigurator {

    fun apply(mapper: ObjectMapper) {
        applyConfigure(mapper)
        applyProvider(mapper)
        applyModule(mapper)
        applyCustomizer(mapper)
    }


    fun applyConfigure(mapper: ObjectMapper)

    fun applyProvider(mapper: ObjectMapper)

    fun applyModule(mapper: ObjectMapper)

    fun applyCustomizer(mapper: ObjectMapper)

}
