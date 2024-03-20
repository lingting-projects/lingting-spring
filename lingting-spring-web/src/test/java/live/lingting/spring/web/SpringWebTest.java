package live.lingting.spring.web;

import com.fasterxml.jackson.core.type.TypeReference;
import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.framework.jackson.JacksonUtils;
import live.lingting.spring.web.properties.SpringWebProperties;
import live.lingting.spring.web.scope.WebScope;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lingting 2024-03-20 17:29
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringWebTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private SpringWebProperties properties;

	@SneakyThrows
	@Test
	void test() {
		mock.perform(get("/hello"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value(ApiResultCode.SUCCESS.getMessage()))

		;

		mock.perform(get("/pagination?page=2&size=30&sort=id,desc"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.page").value(2))
			.andExpect(jsonPath("$.data.size").value(30))
			.andExpect(jsonPath("$.data.sorts[0].field").value("id"))
			.andExpect(jsonPath("$.data.sorts[0].desc").value(true))

		;

		mock.perform(get("/exception"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(500))
			.andExpect(jsonPath("$.message").value(ApiResultCode.SERVER_ERROR.getMessage()))

		;

		mock.perform(get("/validation"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(400))
			.andExpect(jsonPath("$.message").value(SpringTestController.P.MESSAGE))

		;

		mock.perform(get("/validation?name=lingting"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data.name").value("lingting"))

		;

		mock.perform(get("/scope"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data.uri").value("/scope"))
			.andExpect(result -> {
				MockHttpServletResponse response = result.getResponse();
				String content = response.getContentAsString();
				WebScope scope = JacksonUtils.toObj(content, new TypeReference<R<WebScope>>() {
				}).data();

				assertEquals(scope.getTraceId(), response.getHeader(properties.getHeaderTraceId()));
				assertEquals(scope.getRequestId(), response.getHeader(properties.getHeaderRequestId()));
			})

		;
	}

}
