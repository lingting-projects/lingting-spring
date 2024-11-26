package live.lingting.spring.security.web

import live.lingting.framework.api.ApiResultCode
import live.lingting.framework.jackson.JacksonUtils.toObj
import live.lingting.framework.security.domain.AuthorizationVO
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.spring.security.web.properties.SecurityWebProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author lingting 2024-03-21 20:19
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityWebTest {
    @Autowired
    private val mock: MockMvc? = null

    @Autowired
    private val properties: SecurityWebProperties? = null

    @Autowired
    private val securityPassword: SecurityPassword? = null

    @Test
    fun test() {
        val password = securityPassword!!.encodeFront("lingting")

        mock!!.perform(delete("/authorization/logout"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))

        mock.perform(get("/authorization/password?username=lingting_error&password=$password"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))

        mock.perform(get("/authorization/resolve"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))

        mock.perform(get("/authorization/refresh"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))

        val json: String = mock.perform(get("/authorization/password?username=lingting&password=$password"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("authorization").isNotEmpty())
            .andExpect(jsonPath("username").value("lingting"))
            .andReturn()
            .response
            .contentAsString

        val vo: AuthorizationVO = toObj<AuthorizationVO>(json, AuthorizationVO::class.java)
        Assertions.assertEquals("lingting", vo.nickname)

        mock.perform(get("/authorization/resolve").header(properties!!.headerAuthorization, vo.authorization))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("authorization").value(vo.authorization))
            .andExpect(jsonPath("username").value("lingting"))

        mock.perform(delete("/authorization/logout").header(properties.headerAuthorization, vo.authorization))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("authorization").value(vo.authorization))

        mock.perform(get("/authorization/resolve").header(properties.headerAuthorization, vo.authorization))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))
    }
}
