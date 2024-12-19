package live.lingting.spring.grpc.properties

import java.time.Duration
import live.lingting.framework.util.MdcUtils
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-02-05 16:10
 */
@ConfigurationProperties(prefix = GrpcSpringProperties.PREFIX)
class GrpcSpringProperties {

    companion object {
        const val PREFIX: String = "lingting.grpc"
    }

    var traceIdKey: String = MdcUtils.traceIdKey

    var traceOrder: Int = Int.MIN_VALUE + 100

    var keepAliveTime: Duration = Duration.ofMinutes(30)

    var keepAliveTimeout: Duration = Duration.ofSeconds(2)

    var useGzip: Boolean = false

    var useCustomizeExecute: Boolean = true

    var client: Client = Client()

    var server: Server = Server()

    class Client {
        var host: String? = null

        var port: Int = 80

        var usePlaintext: Boolean = false

        /**
         * 是否关闭ssl校验,仅在不使用明文时生效
         */
        var disableSsl: Boolean = false

        var enableRetry: Boolean = true

        var enableKeepAlive: Boolean = true
    }

    class Server {
        var port: Int? = null

        var messageSize: Long = 524288

        var exceptionHandlerOrder: Int = Int.MIN_VALUE + 200
    }

}
