package live.lingting.spring.grpc.properties;

import live.lingting.framework.util.MdcUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lingting 2024-02-05 16:10
 */
@ConfigurationProperties(prefix = GrpcSpringProperties.PREFIX)
public class GrpcSpringProperties {

	public static final String PREFIX = "lingting.grpc";

	private String traceIdKey = MdcUtils.TRACE_ID;

	private int traceOrder = Integer.MIN_VALUE + 100;

	private Duration keepAliveTime = Duration.ofMinutes(30);

	private Duration keepAliveTimeout = Duration.ofSeconds(2);

	private Client client = new Client();

	private Server server = new Server();

	public String getTraceIdKey() {
		return this.traceIdKey;
	}

	public int getTraceOrder() {
		return this.traceOrder;
	}

	public Duration getKeepAliveTime() {
		return this.keepAliveTime;
	}

	public Duration getKeepAliveTimeout() {
		return this.keepAliveTimeout;
	}

	public Client getClient() {
		return this.client;
	}

	public Server getServer() {
		return this.server;
	}

	public void setTraceIdKey(String traceIdKey) {
		this.traceIdKey = traceIdKey;
	}

	public void setTraceOrder(int traceOrder) {
		this.traceOrder = traceOrder;
	}

	public void setKeepAliveTime(Duration keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public void setKeepAliveTimeout(Duration keepAliveTimeout) {
		this.keepAliveTimeout = keepAliveTimeout;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setServer(Server server) {
		this.server = server;
	}

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

		public String getHost() {
			return this.host;
		}

		public Integer getPort() {
			return this.port;
		}

		public boolean isUsePlaintext() {
			return this.usePlaintext;
		}

		public boolean isDisableSsl() {
			return this.disableSsl;
		}

		public boolean isEnableRetry() {
			return this.enableRetry;
		}

		public boolean isEnableKeepAlive() {
			return this.enableKeepAlive;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public void setUsePlaintext(boolean usePlaintext) {
			this.usePlaintext = usePlaintext;
		}

		public void setDisableSsl(boolean disableSsl) {
			this.disableSsl = disableSsl;
		}

		public void setEnableRetry(boolean enableRetry) {
			this.enableRetry = enableRetry;
		}

		public void setEnableKeepAlive(boolean enableKeepAlive) {
			this.enableKeepAlive = enableKeepAlive;
		}

	}

	public static class Server {

		private Integer port;

		private long messageSize = 524288;

		private int exceptionHandlerOrder = Integer.MIN_VALUE + 200;

		public Integer getPort() {
			return this.port;
		}

		public long getMessageSize() {
			return this.messageSize;
		}

		public int getExceptionHandlerOrder() {
			return this.exceptionHandlerOrder;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public void setMessageSize(long messageSize) {
			this.messageSize = messageSize;
		}

		public void setExceptionHandlerOrder(int exceptionHandlerOrder) {
			this.exceptionHandlerOrder = exceptionHandlerOrder;
		}

	}

}
