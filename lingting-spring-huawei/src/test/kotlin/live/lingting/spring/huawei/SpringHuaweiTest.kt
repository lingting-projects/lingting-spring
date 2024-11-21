package live.lingting.spring.huawei

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-09-18 20:55
 */
@SpringBootTest
internal class SpringHuaweiTest {
    @Autowired
    var iam: HuaweiIam = null

    @Autowired
    var obsBucket: HuaweiObsBucket = null

    @Test
    fun test() {
        Assertions.assertNotNull(iam)
        Assertions.assertNotNull(obsBucket)
        val obsObject: HuaweiObsObject = obsBucket.use("key")
        Assertions.assertTrue(obsObject.publicUrl().contains("key"))
    }
}
