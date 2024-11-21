package live.lingting.spring.grpc

import live.lingting.framework.grpc.GrpcServer
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-26 10:10
 */
@SpringBootTest
class SpringGrpcTest {
    @Autowired
    private val server: GrpcServer? = null

    @Test
    fun test() {
        assertTrue(server!!.port() > -1)
        assertTrue(server.fullMethodNameMap.isEmpty())
    }
}
