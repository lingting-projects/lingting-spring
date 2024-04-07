package live.lingting.spring.security.web.resource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.lingting.framework.Sequence;
import live.lingting.framework.security.authorize.SecurityAuthorize;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author lingting 2024-03-21 19:59
 */
@RequiredArgsConstructor
public class SecurityWebResourceInterceptor implements HandlerInterceptor, Ordered, Sequence {

	protected static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	protected final SecurityWebProperties properties;

	protected final SecurityAuthorize authorize;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod handlerMethod && !isIgnoreUri(request.getRequestURI())) {
			validAuthority(handlerMethod);
		}

		return true;
	}

	protected void validAuthority(HandlerMethod handlerMethod) {
		Class<?> cls = handlerMethod.getBeanType();
		Method method = handlerMethod.getMethod();
		authorize.valid(cls, method);
	}

	protected boolean isIgnoreUri(String uri) {
		Set<String> ignoreAntPatterns = properties.getIgnoreUris();
		if (CollectionUtils.isEmpty(ignoreAntPatterns)) {
			return false;
		}

		for (String antPattern : ignoreAntPatterns) {
			if (ANT_PATH_MATCHER.match(antPattern, uri)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getSequence() {
		return getOrder();
	}

	@Override
	public int getOrder() {
		return authorize.getOrder();
	}

}
