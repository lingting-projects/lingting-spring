package live.lingting.spring.ali;

import live.lingting.framework.ali.AliOssBucket;
import live.lingting.framework.ali.AliOssObject;
import live.lingting.framework.ali.AliSts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
class SpringHuaweiTest {

	@Autowired
	AliSts sts;

	@Autowired
	AliOssBucket ossBucket;

	@Test
	void test() {
		assertNotNull(sts);
		assertNotNull(ossBucket);
		AliOssObject obsObject = ossBucket.ossObject("key");
		assertTrue(obsObject.publicUrl().contains("key"));
	}

}
