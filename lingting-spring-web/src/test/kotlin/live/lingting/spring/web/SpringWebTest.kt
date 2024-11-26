package live.lingting.spring.web

import com.fasterxml.jackson.core.type.TypeReference
import live.lingting.framework.api.ApiResultCode
import live.lingting.framework.api.R
import live.lingting.framework.jackson.JacksonUtils
import live.lingting.spring.web.properties.SpringWebProperties
import live.lingting.spring.web.scope.WebScope
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author lingting 2024-03-20 17:29
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringWebTest {
    @Autowired
    private val mock: MockMvc? = null

    @Autowired
    private val properties: SpringWebProperties? = null

    @Test
    fun test() {
        mock!!.perform(get("/hello"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value(ApiResultCode.SUCCESS.message))

        mock.perform(get("/pagination?page=2&size=30&sort=id,desc"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.page").value(2))
            .andExpect(jsonPath("$.data.size").value(30))
            .andExpect(jsonPath("$.data.sorts[0].field").value("id"))
            .andExpect(jsonPath("$.data.sorts[0].desc").value(true))

        mock.perform(get("/exception"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.message").value(ApiResultCode.SERVER_ERROR.message))

        mock.perform(get("/validation"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value(SpringTestController.P.MESSAGE))

        mock.perform(get("/validation?name=lingting"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.name").value("lingting"))

        mock.perform(get("/scope"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.uri").value("/scope"))
            .andExpect { result ->
                val response: MockHttpServletResponse = result.response
                val content: String = response.contentAsString
                val scope = JacksonUtils.toObj(content, object : TypeReference<R<WebScope>>() {
                }).data

                assertEquals(scope!!.traceId, response.getHeader(properties!!.headerTraceId))
                assertEquals(scope.requestId, response.getHeader(properties.headerRequestId))
            }
    }
}
