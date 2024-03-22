package live.lingting.spring.security.web.properties;

import live.lingting.spring.security.properties.SecuritySpringProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author lingting 2024-03-20 19:50
 */
@Data
@ConfigurationProperties(SecurityWebProperties.PREFIX)
public class SecurityWebProperties {

	public static final String PREFIX = SecuritySpringProperties.PREFIX + ".web";

	private Set<String> ignoreUris;

	private String headerAuthorization = "Authorization";

	private String paramAuthorization = "token";

}
