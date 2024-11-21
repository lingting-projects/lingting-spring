package live.lingting.spring.grpc.properties

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-02-05 16:10
 */
@ConfigurationProperties(prefix = GrpcSpringProperties.PREFIX)
class GrpcSpringProperties {
    var traceIdKey: String = TRACE_ID

    var traceOrder: Int = Int.MIN_VALUE + 100

    var keepAliveTime: Duration = Duration.ofMinutes(30)

    var keepAliveTimeout: Duration = Duration.ofSeconds(2)

    var client: Client = Client()

    var server: Server = Server()

    class Client {
        var host: String = null

        var port: Int = 80

        var isUsePlaintext: Boolean = false

        /**
         * 是否关闭ssl校验,仅在不使用明文时生效
         */
        var isDisableSsl: Boolean = false

        var isEnableRetry: Boolean = true

        var isEnableKeepAlive: Boolean = true
    }

    class Server {
        var port: Int = null

        var messageSize: Long = 524288

        var exceptionHandlerOrder: Int = Int.MIN_VALUE + 200
    }

    companion object {
        const val PREFIX: String = "lingting.grpc"
    }
}
