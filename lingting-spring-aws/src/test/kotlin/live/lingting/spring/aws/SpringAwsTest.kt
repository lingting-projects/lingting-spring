package live.lingting.spring.aws

import live.lingting.framework.aws.AwsS3Bucket
import live.lingting.framework.aws.AwsS3Object
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
class SpringAwsTest {
    @Autowired
    var s3Bucket: AwsS3Bucket? = null

    @Test
    fun test() {
        Assertions.assertNotNull(s3Bucket)
        val obsObject: AwsS3Object = s3Bucket!!.use("key")
        Assertions.assertTrue(obsObject.publicUrl().contains("key"))
    }
}
