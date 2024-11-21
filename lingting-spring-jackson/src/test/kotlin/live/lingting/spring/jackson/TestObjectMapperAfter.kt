package live.lingting.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-02-02 16:00
 */
@Component
class TestObjectMapperAfter : ObjectMapperAfter {
    var isAfter: Boolean = false
        private set

    override fun apply(mapper: ObjectMapper) {
        Assertions.assertFalse(this.isAfter)
        this.isAfter = true
    }
}
