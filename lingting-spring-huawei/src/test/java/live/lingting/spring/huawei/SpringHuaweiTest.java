package live.lingting.spring.huawei;

import live.lingting.framework.huawei.HuaweiIam;
import live.lingting.framework.huawei.HuaweiObsBucket;
import live.lingting.framework.huawei.HuaweiObsObject;
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
	HuaweiIam iam;

	@Autowired
	HuaweiObsBucket obsBucket;

	@Test
	void test() {
		assertNotNull(iam);
		assertNotNull(obsBucket);
		HuaweiObsObject obsObject = obsBucket.use("key");
		assertTrue(obsObject.publicUrl().contains("key"));
	}

}
