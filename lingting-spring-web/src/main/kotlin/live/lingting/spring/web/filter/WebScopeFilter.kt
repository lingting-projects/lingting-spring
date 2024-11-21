package live.lingting.spring.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.util.MdcUtils.fillTraceId
import live.lingting.framework.util.MdcUtils.removeTraceId
import live.lingting.framework.util.MdcUtils.traceId
import live.lingting.spring.web.properties.SpringWebProperties
import live.lingting.spring.web.scope.WebScopeHolder
import live.lingting.spring.web.wrapper.RepeatBodyRequestWrapper
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * @author lingting 2024-03-20 14:57
 */
class WebScopeFilter(private val properties: SpringWebProperties) : OncePerRequestFilter() {

    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val request: RepeatBodyRequestWrapper = RepeatBodyRequestWrapper.of(httpServletRequest)
        val response = ContentCachingResponseWrapper(httpServletResponse)

        val traceId = traceId(request)
        val requestId = requestId()

        val scope = WebScopeHolder.of(request, traceId, requestId)
        WebScopeHolder.put(scope)
        fillTraceId(scope.traceId)
        try {
            response.addHeader(properties.headerTraceId, scope.traceId)
            response.addHeader(properties.headerRequestId, scope.requestId)
            filterChain.doFilter(request, response)
        } finally {
            // 写入返回值
            response.copyBodyToResponse()
            // 关闭包装请求
            request.close()
            WebScopeHolder.pop()
            removeTraceId()
        }
    }

    protected fun traceId(request: HttpServletRequest): String {
        val traceId = request.getHeader(properties.headerTraceId)
        if (StringUtils.hasText(traceId)) {
            return traceId
        }
        return requestId()
    }

    protected fun requestId(): String {
        return traceId()
    }
}
