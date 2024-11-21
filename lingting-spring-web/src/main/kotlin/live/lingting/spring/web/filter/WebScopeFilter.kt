package live.lingting.spring.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.lingting.framework.util.MdcUtils;
import live.lingting.spring.web.properties.SpringWebProperties;
import live.lingting.spring.web.scope.WebScope;
import live.lingting.spring.web.scope.WebScopeHolder;
import live.lingting.spring.web.wrapper.RepeatBodyRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @author lingting 2024-03-20 14:57
 */
public class WebScopeFilter extends OncePerRequestFilter {

	private final SpringWebProperties properties;

	public WebScopeFilter(SpringWebProperties properties) {
		this.properties = properties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		final RepeatBodyRequestWrapper request = RepeatBodyRequestWrapper.of(httpServletRequest);
		final ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(httpServletResponse);

		String traceId = traceId(request);
		String requestId = requestId();

		WebScope scope = WebScopeHolder.of(request, traceId, requestId);
		WebScopeHolder.put(scope);
		MdcUtils.fillTraceId(scope.getTraceId());
		try {
			response.addHeader(properties.getHeaderTraceId(), scope.getTraceId());
			response.addHeader(properties.getHeaderRequestId(), scope.getRequestId());
			filterChain.doFilter(request, response);
		}
		finally {
			// 写入返回值
			response.copyBodyToResponse();
			// 关闭包装请求
			request.close();
			WebScopeHolder.pop();
			MdcUtils.removeTraceId();
		}
	}

	protected String traceId(HttpServletRequest request) {
		String traceId = request.getHeader(properties.getHeaderTraceId());
		if (StringUtils.hasText(traceId)) {
			return traceId;
		}
		return requestId();
	}

	protected String requestId() {
		return MdcUtils.traceId();
	}

}
