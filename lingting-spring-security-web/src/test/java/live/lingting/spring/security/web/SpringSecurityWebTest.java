package live.lingting.spring.security.web;

import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.jackson.JacksonUtils;
import live.lingting.framework.security.domain.AuthorizationVO;
import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lingting 2024-03-21 20:19
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityWebTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private SecurityWebProperties properties;

	@Autowired
	private SecurityPassword securityPassword;

	@SneakyThrows
	@Test
	void test() {
		String password = securityPassword.encodeFront("lingting");

		mock.perform(delete("/authorization/logout"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.getCode()));

		mock.perform(get("/authorization/password?username=lingting_error&password=" + password))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.getCode()))

		;

		mock.perform(get("/authorization/resolve"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.getCode()))

		;

		mock.perform(get("/authorization/refresh"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.getCode()));

		String json = mock.perform(get("/authorization/password?username=lingting&password=" + password))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("token").isNotEmpty())
			.andExpect(jsonPath("username").value("lingting"))
			.andReturn()
			.getResponse()
			.getContentAsString()

		;

		AuthorizationVO vo = JacksonUtils.toObj(json, AuthorizationVO.class);
		assertEquals("lingting", vo.getNickname());

		mock.perform(get("/authorization/resolve").header(properties.getHeaderAuthorization(), vo.getToken()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("token").value(vo.getToken()))
			.andExpect(jsonPath("username").value("lingting"))

		;

		mock.perform(delete("/authorization/logout").header(properties.getHeaderAuthorization(), vo.getToken()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("token").value(vo.getToken()))

		;

		mock.perform(get("/authorization/resolve").header(properties.getHeaderAuthorization(), vo.getToken()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("code").value(ApiResultCode.UNAUTHORIZED_ERROR.getCode()))

		;

	}

}
