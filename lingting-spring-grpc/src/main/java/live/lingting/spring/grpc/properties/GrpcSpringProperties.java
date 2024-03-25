package live.lingting.spring.grpc.properties;

import live.lingting.framework.util.MdcUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lingting 2024-02-05 16:10
 */
@Data
@ConfigurationProperties(prefix = GrpcSpringProperties.PREFIX)
public class GrpcSpringProperties {

	public static final String PREFIX = "lingting.grpc";

	private String traceIdKey = MdcUtils.TRACE_ID;

	private Duration keepAliveTime = Duration.ofMinutes(30);

	private Duration keepAliveTimeout = Duration.ofSeconds(2);

	private Client client = new Client();

	private Server server = new Server();

	@Data
	public static class Client {

		private String host;

		private Integer port = 80;

		private boolean usePlaintext = false;

		/**
		 * 是否关闭ssl校验,仅在不使用明文时生效
		 */
		private boolean disableSsl = false;

		private boolean enableRetry = true;

		private boolean enableKeepAlive = true;

	}

	@Data
	public static class Server {

		private Integer port;

		private long messageSize = 524288;

	}

}
