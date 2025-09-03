package live.lingting.spring.web.scope

import jakarta.servlet.http.HttpServletRequest
import live.lingting.framework.context.StackContext
import live.lingting.framework.http.HttpUrlBuilder
import live.lingting.framework.util.IpUtils.getFirstIp
import live.lingting.framework.util.ServletUtils
import java.util.Optional
import java.util.function.Function

/**
 * @author lingting 2024-03-20 15:09
 */
object WebScopeHolder {
    val LOCAL = StackContext<WebScope>()

    @JvmStatic
    fun get(): WebScope? {
        return LOCAL.peek()
    }

    val optional: Optional<WebScope>
        get() = Optional.ofNullable<WebScope>(LOCAL.peek())

    @JvmStatic
    fun put(webScope: WebScope) {
        LOCAL.push(webScope)
    }

    @JvmStatic
    fun pop() {
        LOCAL.pop()
    }

    @JvmStatic
    fun of(request: HttpServletRequest, traceId: String, requestId: String): WebScope {
        val headers = ServletUtils.headers(request)
        val origin = ServletUtils.origin(request, headers)
        var scheme = request.scheme
        var host = headers.hostReal()

        if (!origin.isNullOrBlank()) {
            val builder = HttpUrlBuilder.from(origin)
            scheme = builder.scheme()
            host = builder.headerHost()
        }

        return WebScope(
            scheme,
            host ?: "",
            origin ?: "",
            getFirstIp(request),
            request.requestURI,
            traceId,
            requestId,
            headers.language() ?: "",
            headers.authorization() ?: "",
            headers.ua() ?: "",
            headers
        )
    }

    @JvmStatic
    fun <T> map(function: Function<WebScope, T>): T? {
        return optional.map(function).orElse(null)
    }

    @JvmStatic
    fun <T> map(function: Function<WebScope, T>, other: T): T {
        return optional.map<T>(function).orElse(other)
    }

    @JvmStatic
    fun uri(): String {
        return map<String>(Function { obj -> obj.uri }, "")
    }

}
