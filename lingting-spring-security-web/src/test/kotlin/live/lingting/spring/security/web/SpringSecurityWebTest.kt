package live.lingting.spring.security.web

import live.lingting.framework.security.domain.AuthorizationVO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

/**
 * @author lingting 2024-03-21 20:19
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityWebTest {
    @Autowired
    private val mock: MockMvc = null

    @Autowired
    private val properties: SecurityWebProperties = null

    @Autowired
    private val securityPassword: SecurityPassword = null

    @Test
    fun test() {
        val password = securityPassword.encodeFront("lingting")

        mock.perform(delete("/authorization/logout"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))

        mock.perform(get("/authorization/passwordusername=lingting_error&password=" + password))
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

        val json: String = mock.perform(get("/authorization/passwordusername=lingting&password=" + password))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").isNotEmpty())
            .andExpect(jsonPath("username").value("lingting"))
            .andReturn()
            .getResponse()
            .getContentAsString()


        val vo: AuthorizationVO = toObj<AuthorizationVO>(json, AuthorizationVO::class.java)
        Assertions.assertEquals("lingting", vo.nickname)

        mock.perform(get("/authorization/resolve").header(properties.headerAuthorization, vo.token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").value(vo.token))
            .andExpect(jsonPath("username").value("lingting"))



        mock.perform(delete("/authorization/logout").header(properties.headerAuthorization, vo.token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").value(vo.token))



        mock.perform(get("/authorization/resolve").header(properties.headerAuthorization, vo.token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.code))
    }
}
