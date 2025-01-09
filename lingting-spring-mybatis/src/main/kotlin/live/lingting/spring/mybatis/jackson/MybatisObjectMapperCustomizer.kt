package live.lingting.spring.mybatis.jackson

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.fasterxml.jackson.databind.ObjectMapper
import live.lingting.spring.jackson.ObjectMapperCustomizer

/**
 * @author lingting 2024/11/27 15:16
 */
open class MybatisObjectMapperCustomizer : ObjectMapperCustomizer {

    override fun apply(mapper: ObjectMapper) {
        JacksonTypeHandler.setObjectMapper(mapper)
    }

}
