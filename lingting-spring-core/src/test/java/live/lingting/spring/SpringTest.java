package live.lingting.spring;

import live.lingting.spring.util.EnvironmentUtils;
import live.lingting.spring.util.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-02-02 13:54
 */
@SpringBootTest
class SpringTest {

	@Test
	void test() {
		assertTrue(EnvironmentUtils.isActiveProfiles("test"));
		Map<String, SpringTestBean> beans = SpringUtils.getBeansOfType(SpringTestBean.class);
		assertEquals(1, beans.size());
		SpringTestBean bean = beans.values().iterator().next();
		assertEquals("bean", bean.getValue());
		assertTrue(bean.isStart());
	}

}
