package live.lingting.spring.ali;

import live.lingting.framework.ali.AliOssBucket;
import live.lingting.framework.ali.AliOssObject;
import live.lingting.framework.ali.AliSts;
import live.lingting.framework.aws.AwsS3Bucket;
import live.lingting.framework.aws.s3.interfaces.AwsS3BucketInterface;
import live.lingting.spring.util.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
class SpringAliTest {

	@Autowired
	AliSts sts;

	@Autowired
	AliOssBucket ossBucket;

	@Test
	void test() {
		assertNotNull(sts);
		assertNotNull(ossBucket);
		AliOssObject obsObject = ossBucket.use("key");
		assertTrue(obsObject.publicUrl().contains("key"));
		Map<String, AwsS3Bucket> awsS3BucketMap = SpringUtils.getBeansOfType(AwsS3Bucket.class);
		assertEquals(1, awsS3BucketMap.size());
		Map<String, AwsS3BucketInterface> awsS3BucketInterfaceMap = SpringUtils
			.getBeansOfType(AwsS3BucketInterface.class);
		assertEquals(2, awsS3BucketInterfaceMap.size());
	}

}
