package live.lingting.spring.web.scope;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2024-03-20 15:02
 */
@Getter
@RequiredArgsConstructor
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

	public void putAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	public <T> T getAttribute(String key) {
		return (T) this.attributes.get(key);
	}

}
