package live.lingting.spring.aws;

import live.lingting.framework.aws.AwsS3Bucket;
import live.lingting.framework.aws.AwsS3Object;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
class SpringAwsTest {

	@Autowired
	AwsS3Bucket s3Bucket;

	@Test
	void test() {
		assertNotNull(s3Bucket);
		AwsS3Object obsObject = s3Bucket.use("key");
		assertTrue(obsObject.publicUrl().contains("key"));
	}

}
