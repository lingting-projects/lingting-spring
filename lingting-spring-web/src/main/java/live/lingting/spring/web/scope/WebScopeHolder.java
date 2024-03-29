package live.lingting.spring.web.scope;

import jakarta.servlet.http.HttpServletRequest;
import live.lingting.framework.util.IpUtils;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author lingting 2024-03-20 15:09
 */
@UtilityClass
public class WebScopeHolder {

	static final ThreadLocal<WebScope> LOCAL = new ThreadLocal<>();

	public static WebScope get() {
		return LOCAL.get();
	}

	public static Optional<WebScope> getOptional() {
		return Optional.ofNullable(LOCAL.get());
	}

	public static void set(WebScope webScope) {
		LOCAL.set(webScope);
	}

	public static void remove() {
		LOCAL.remove();
	}

	public static WebScope of(HttpServletRequest request, String traceId, String requestId) {
		return new WebScope(request.getScheme(), request.getHeader("Host"), request.getHeader("Origin"),
				IpUtils.getFirstIp(request), request.getRequestURI(), traceId, requestId,
				request.getHeader("Accept-Language"), request.getHeader("Authorization"),
				request.getHeader("User-Agent"));
	}

	public static <T> T map(Function<WebScope, T> function) {
		return map(function, null);
	}

	public static <T> T map(Function<WebScope, T> function, T other) {
		return getOptional().map(function).orElse(other);
	}

	public static String uri() {
		return map(WebScope::getUri, "");
	}

}
