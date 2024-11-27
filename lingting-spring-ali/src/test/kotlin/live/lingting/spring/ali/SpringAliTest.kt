package live.lingting.spring.ali

import live.lingting.framework.ali.AliOssBucket
import live.lingting.framework.ali.AliSts
import live.lingting.framework.aws.AwsS3Bucket
import live.lingting.framework.aws.s3.interfaces.AwsS3BucketInterface
import live.lingting.spring.util.SpringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
class SpringAliTest {
    @Autowired
    var sts: AliSts? = null

    @Autowired
    var ossBucket: AliOssBucket? = null

    @Test
    fun test() {
        assertNotNull(sts)
        assertNotNull(ossBucket)
        val obsObject = ossBucket!!.use("key")
        assertTrue(obsObject.publicUrl().contains("key"))
        val awsS3BucketMap = SpringUtils.getBeansOfType(AwsS3Bucket::class.java)
        assertEquals(1, awsS3BucketMap.size)
        val awsS3BucketInterfaceMap = SpringUtils.getBeansOfType(AwsS3BucketInterface::class.java)
        assertEquals(2, awsS3BucketInterfaceMap.size)
    }
}
