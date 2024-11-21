package live.lingting.spring.web.scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2024-03-20 15:02
 */
public class WebScope {

	private final String scheme;

	private final String host;

	private final String origin;

	private final String ip;

	private final String uri;

	private final String traceId;

	private final String requestId;

	private final String language;

	private final String authorization;

	private final String userAgent;

	private final Map<String, Object> attributes = new HashMap<>();

	public WebScope(String scheme, String host, String origin, String ip, String uri, String traceId, String requestId,
			String language, String authorization, String userAgent) {
		this.scheme = scheme;
		this.host = host;
		this.origin = origin;
		this.ip = ip;
		this.uri = uri;
		this.traceId = traceId;
		this.requestId = requestId;
		this.language = language;
		this.authorization = authorization;
		this.userAgent = userAgent;
	}

	public void putAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	public <T> T getAttribute(String key) {
		return (T) this.attributes.get(key);
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getHost() {
		return this.host;
	}

	public String getOrigin() {
		return this.origin;
	}

	public String getIp() {
		return this.ip;
	}

	public String getUri() {
		return this.uri;
	}

	public String getTraceId() {
		return this.traceId;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getAuthorization() {
		return this.authorization;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

}
