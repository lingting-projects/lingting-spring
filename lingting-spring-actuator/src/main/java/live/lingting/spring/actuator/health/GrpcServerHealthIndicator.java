package live.lingting.spring.actuator.health;

import live.lingting.framework.grpc.GrpcServer;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

/**
 * @author lingting 2023-11-23 21:29
 */
public class GrpcServerHealthIndicator extends AbstractHealthIndicator {

	private final GrpcServer server;

	public GrpcServerHealthIndicator(GrpcServer server) {
		this.server = server;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		builder.status(server.isRunning() ? Status.UP : Status.DOWN)
			.withDetail("Port", Integer.toString(server.port()));
	}

}
