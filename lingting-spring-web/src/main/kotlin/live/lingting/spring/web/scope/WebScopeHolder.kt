package live.lingting.spring.web.scope

import jakarta.servlet.http.HttpServletRequest
import java.util.Optional
import java.util.function.Function
import live.lingting.framework.thread.StackThreadLocal
import live.lingting.framework.util.HttpUtils
import live.lingting.framework.util.IpUtils.getFirstIp

/**
 * @author lingting 2024-03-20 15:09
 */
object WebScopeHolder {
    val LOCAL: StackThreadLocal<WebScope> = StackThreadLocal<WebScope>()

    @JvmStatic
    fun get(): WebScope? {
        return LOCAL.get()
    }

    val optional: Optional<WebScope>
        get() = Optional.ofNullable<WebScope>(LOCAL.get())

    @JvmStatic
    fun put(webScope: WebScope) {
        LOCAL.put(webScope)
    }

    @JvmStatic
    fun pop() {
        LOCAL.pop()
    }

    @JvmStatic
    fun of(request: HttpServletRequest, traceId: String, requestId: String): WebScope {
        val headers = HttpUtils.headers(request)
        return WebScope(
            request.scheme, headers.host() ?: "", headers.origin() ?: "",
            getFirstIp(request), request.requestURI, traceId, requestId,
            headers.language() ?: "", headers.authorization() ?: "",
            headers.ua() ?: ""
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
