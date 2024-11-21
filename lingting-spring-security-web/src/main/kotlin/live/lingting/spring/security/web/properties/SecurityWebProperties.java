package live.lingting.spring.security.web.properties;

import live.lingting.spring.security.properties.SecuritySpringProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author lingting 2024-03-20 19:50
 */
@ConfigurationProperties(SecurityWebProperties.PREFIX)
public class SecurityWebProperties {

	public static final String PREFIX = SecuritySpringProperties.PREFIX + ".web";

	private Set<String> ignoreUris;

	private String headerAuthorization = "Authorization";

	private String paramAuthorization = "token";

	public Set<String> getIgnoreUris() {
		return this.ignoreUris;
	}

	public String getHeaderAuthorization() {
		return this.headerAuthorization;
	}

	public String getParamAuthorization() {
		return this.paramAuthorization;
	}

	public void setIgnoreUris(Set<String> ignoreUris) {
		this.ignoreUris = ignoreUris;
	}

	public void setHeaderAuthorization(String headerAuthorization) {
		this.headerAuthorization = headerAuthorization;
	}

	public void setParamAuthorization(String paramAuthorization) {
		this.paramAuthorization = paramAuthorization;
	}

}
