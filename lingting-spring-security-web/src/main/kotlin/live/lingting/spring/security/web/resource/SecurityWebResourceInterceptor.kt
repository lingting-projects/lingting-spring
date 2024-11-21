package live.lingting.spring.security.web.resource

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.Sequence
import live.lingting.framework.security.authorize.SecurityAuthorize
import live.lingting.spring.security.web.properties.SecurityWebProperties
import org.springframework.core.Ordered
import org.springframework.util.AntPathMatcher
import org.springframework.util.CollectionUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * @author lingting 2024-03-21 19:59
 */
class SecurityWebResourceInterceptor(
    protected val properties: SecurityWebProperties,
    protected val authorize: SecurityAuthorize
) : HandlerInterceptor, Ordered, Sequence {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && !isIgnoreUri(request.requestURI)) {
            validAuthority(handler)
        }

        return true
    }

    protected fun validAuthority(handlerMethod: HandlerMethod) {
        val cls = handlerMethod.beanType
        val method = handlerMethod.method
        authorize.valid(cls, method)
    }

    protected fun isIgnoreUri(uri: String): Boolean {
        val ignoreAntPatterns = properties.ignoreUris
        if (CollectionUtils.isEmpty(ignoreAntPatterns)) {
            return false
        }

        for (antPattern in ignoreAntPatterns) {
            if (ANT_PATH_MATCHER.match(antPattern, uri)) {
                return true
            }
        }

        return false
    }

    override val sequence: Int = order

    override fun getOrder(): Int = authorize.order

    companion object {
        @JvmField
        protected val ANT_PATH_MATCHER: AntPathMatcher = AntPathMatcher()
    }
}
