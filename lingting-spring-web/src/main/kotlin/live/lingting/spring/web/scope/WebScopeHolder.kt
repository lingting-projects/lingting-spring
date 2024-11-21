package live.lingting.spring.web.scope

import jakarta.servlet.http.HttpServletRequest
import java.util.Optional
import java.util.function.Function
import live.lingting.framework.thread.StackThreadLocal
import live.lingting.framework.util.IpUtils.getFirstIp

/**
 * @author lingting 2024-03-20 15:09
 */
class WebScopeHolder private constructor() {
    init {
        throw UnsupportedOperationException("This is a utility class and cannot be instantiated")
    }

    companion object {
        val LOCAL: StackThreadLocal<WebScope> = StackThreadLocal<WebScope>()

        @JvmStatic
        fun get(): WebScope {
            return LOCAL.get()
        }

        val optional: Optional<WebScope>
            get() = Optional.ofNullable<WebScope>(LOCAL.get())

        fun put(webScope: WebScope) {
            LOCAL.put(webScope)
        }

        fun pop() {
            LOCAL.pop()
        }

        fun of(request: HttpServletRequest, traceId: String, requestId: String): WebScope {
            return WebScope(
                request.getScheme(), request.getHeader("Host"), request.getHeader("Origin"),
                getFirstIp(request), request.getRequestURI(), traceId, requestId,
                request.getHeader("Accept-Language"), request.getHeader("Authorization"),
                request.getHeader("User-Agent")
            )
        }

        fun <T> map(function: Function<WebScope, T>): T {
            return map<T>(function, null)
        }

        fun <T> map(function: Function<WebScope, T>, other: T): T {
            return optional.map<T>(function).orElse(other)
        }

        fun uri(): String {
            return WebScopeHolder.map<String>(java.util.function.Function { obj -> obj.getUri() }, "")!!
        }
    }
}
