package live.lingting.spring.security.web.resource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.lingting.framework.security.domain.SecurityScope;
import live.lingting.framework.security.domain.SecurityToken;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.framework.util.StringUtils;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author lingting 2024-03-21 19:49
 */
@RequiredArgsConstructor
public class SecurityWebResourceFilter extends OncePerRequestFilter {

	private final SecurityWebProperties properties;

	private final SecurityResourceService service;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {
		handlerScope(request);

		try {
			filterChain.doFilter(request, response);
		}
		finally {
			service.removeScope();
		}
	}

	protected void handlerScope(HttpServletRequest request) {
		SecurityToken token = getToken(request);
		// token有效, 设置上下文
		if (!token.isAvailable()) {
			return;
		}
		try {
			SecurityScope scope = service.resolve(token);
			service.setScope(scope);
		}
		catch (Exception e) {
			//
		}
	}

	public SecurityToken getToken(HttpServletRequest request) {
		String raw = request.getHeader(properties.getHeaderAuthorization());

		// 走参数
		if (!StringUtils.hasText(raw)) {
			return getTokenByParams(request);
		}

		return SecurityToken.ofDelimiter(raw, " ");
	}

	public SecurityToken getTokenByParams(HttpServletRequest request) {
		String value = request.getParameter(properties.getParamAuthorization());
		return SecurityToken.ofDelimiter(value, " ");
	}

}
