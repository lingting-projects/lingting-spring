package live.lingting.spring.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.Duration
import live.lingting.framework.i18n.I18n
import live.lingting.framework.time.StopWatch
import live.lingting.framework.util.MdcUtils.fillTraceId
import live.lingting.framework.util.MdcUtils.removeTraceId
import live.lingting.framework.util.MdcUtils.traceId
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.web.properties.SpringWebProperties
import live.lingting.spring.web.request.ServletRequestConsumer
import live.lingting.spring.web.scope.WebScope
import live.lingting.spring.web.scope.WebScopeHolder
import live.lingting.spring.web.wrapper.RepeatBodyRequestWrapper
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * @author lingting 2024-03-20 14:57
 */
open class WebScopeFilter(protected val properties: SpringWebProperties, protected val consumers: List<ServletRequestConsumer>) : OncePerRequestFilter() {

    val log = logger()

    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val request: RepeatBodyRequestWrapper = RepeatBodyRequestWrapper.of(httpServletRequest)
        val response = ContentCachingResponseWrapper(httpServletResponse)
        val traceId = traceId(request)
        val requestId = requestId()
        log.trace("request start. traceId: {}, requestId: {}", traceId, requestId)
        val scope = WebScopeHolder.of(request, traceId, requestId)
        log.trace("request scope: {}", scope)
        WebScopeHolder.put(scope)
        fillTraceId(scope.traceId)
        val language = scope.language()
        if (language != null) {
            log.trace("request language: {}", language)
            I18n.set(language)
        }
        response.addHeader(properties.headerTraceId, scope.traceId)
        response.addHeader(properties.headerRequestId, scope.requestId)
        val watch = StopWatch()
        try {
            watch.start()
            filterChain.doFilter(request, response)
            watch.stop()
            log.trace("request end. traceId: {}, requestId: {}", traceId, requestId)
            consumer(watch.duration(), scope, request, response, null)
        } catch (e: Throwable) {
            // 如果是程序运行异常, 则停止并消费.
            if (watch.isRunning) {
                watch.stop()
                consumer(watch.duration(), scope, request, response, e)
                return
            }
            log.debug("Filter do error.", e)
            // 消费异常直接抛出
            throw e
        } finally {
            // 写入返回值
            response.copyBodyToResponse()
            // 关闭包装请求
            request.close()
            WebScopeHolder.pop()
            I18n.remove()
            removeTraceId()
        }
    }

    protected fun consumer(duration: Duration, scope: WebScope, request: RepeatBodyRequestWrapper, response: ContentCachingResponseWrapper, t: Throwable?) {
        consumers.forEach {
            log.trace("request consumer: {}", it.javaClass.simpleName)
            it.consumer(duration, scope, request, response, t)
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
