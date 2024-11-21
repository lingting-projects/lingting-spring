package live.lingting.spring.ali

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
internal class SpringAliTest {
    @Autowired
    var sts: AliSts = null

    @Autowired
    var ossBucket: AliOssBucket = null

    @Test
    fun test() {
        Assertions.assertNotNull(sts)
        Assertions.assertNotNull(ossBucket)
        val obsObject: AliOssObject = ossBucket.use("key")
        Assertions.assertTrue(obsObject.publicUrl().contains("key"))
        val awsS3BucketMap: MutableMap<String, AwsS3Bucket> = SpringUtils.getBeansOfType(AwsS3Bucket::class.java)
        Assertions.assertEquals(1, awsS3BucketMap.size)
        val awsS3BucketInterfaceMap: MutableMap<String, AwsS3BucketInterface> = SpringUtils
            .getBeansOfType(AwsS3BucketInterface::class.java)
        Assertions.assertEquals(2, awsS3BucketInterfaceMap.size)
    }
}
