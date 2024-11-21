package live.lingting.spring.grpc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-26 10:10
 */
@SpringBootTest
internal class SpringGrpcTest {
    @Autowired
    private val server: GrpcServer = null

    @Test
    fun test() {
        Assertions.assertTrue(server.port() > -1)
        Assertions.assertTrue(server.fullMethodNameMap.isEmpty())
    }
}
