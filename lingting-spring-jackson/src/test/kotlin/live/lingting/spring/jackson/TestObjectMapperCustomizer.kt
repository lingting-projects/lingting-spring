package live.lingting.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-02-02 15:59
 */
@Component
class TestObjectMapperCustomizer : ObjectMapperCustomizer {
    var objectMapper: ObjectMapper? = null
        private set

    override fun apply(mapper: ObjectMapper) {
        objectMapper = mapper.copy()
    }
}
