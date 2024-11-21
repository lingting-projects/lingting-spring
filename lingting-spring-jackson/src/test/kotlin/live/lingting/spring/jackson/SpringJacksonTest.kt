package live.lingting.spring.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.lingting.framework.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lingting 2024-02-02 15:56
 */
@SpringBootTest
class SpringJacksonTest {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TestObjectMapperCustomizer customizer;

	@Autowired
	private TestObjectMapperAfter after;

	@Test
	void test() {
		assertEquals(JacksonUtils.getMapper(), mapper);
		assertEquals(customizer.getObjectMapper(), mapper);
		assertTrue(after.isAfter());
	}

}
