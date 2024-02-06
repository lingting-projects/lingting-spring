package live.lingting.spring.security.grpc.properties;

import live.lingting.framework.properties.SecurityGrpcProperties;
import live.lingting.spring.security.properties.SecuritySpringProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2024-02-05 19:14
 */
@Data
@ConfigurationProperties(prefix = SecurityGrpcSpringProperties.PREFIX)
public class SecurityGrpcSpringProperties {

	public static final String PREFIX = SecuritySpringProperties.PREFIX + ".grpc";

	private String authorizationKey = "Authorization";

	public SecurityGrpcProperties properties() {
		SecurityGrpcProperties properties = new SecurityGrpcProperties();
		properties.setAuthorizationKey(authorizationKey);
		return properties;
	}

}
