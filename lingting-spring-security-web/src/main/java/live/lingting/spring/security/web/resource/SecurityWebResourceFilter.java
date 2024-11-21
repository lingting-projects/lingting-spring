package live.lingting.spring.security.web.resource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.lingting.framework.security.domain.SecurityScope;
import live.lingting.framework.security.domain.SecurityToken;
import live.lingting.framework.security.exception.AuthorizationException;
import live.lingting.framework.security.exception.PermissionsException;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.framework.util.StringUtils;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author lingting 2024-03-21 19:49
 */
public class SecurityWebResourceFilter extends OncePerRequestFilter {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SecurityWebResourceFilter.class);

	private final SecurityWebProperties properties;

	private final SecurityResourceService service;

	public SecurityWebResourceFilter(SecurityWebProperties properties, SecurityResourceService service) {
		this.properties = properties;
		this.service = service;
	}

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {
		SecurityScope scope = getScope(request);
		service.putScope(scope);
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			service.popScope();
		}
	}

	protected SecurityScope getScope(HttpServletRequest request) {
		SecurityToken token = getToken(request);
		// token有效, 设置上下文
		if (!token.isAvailable()) {
			return null;
		}
		try {
			return service.resolve(token);
		}
		catch (AuthorizationException | PermissionsException e) {
			return null;
		}
		catch (Exception e) {
			log.debug("resolve token error!", e);
		}
		return null;
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
