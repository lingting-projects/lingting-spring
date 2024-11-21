package live.lingting.spring.security.web.resource

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.security.domain.SecurityScope
import live.lingting.framework.security.domain.SecurityToken
import live.lingting.framework.security.exception.AuthorizationException
import live.lingting.framework.security.exception.PermissionsException
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.framework.util.StringUtils.hasText
import live.lingting.spring.security.web.properties.SecurityWebProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author lingting 2024-03-21 19:49
 */
class SecurityWebResourceFilter(private val properties: SecurityWebProperties, private val service: SecurityResourceService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val scope = getScope(request)
        service.putScope(scope)
        try {
            filterChain.doFilter(request, response)
        } finally {
            service.popScope()
        }
    }

    protected fun getScope(request: HttpServletRequest): SecurityScope {
        val token = getToken(request)
        // token有效, 设置上下文
        if (!token.isAvailable) {
            return null
        }
        try {
            return service.resolve(token)
        } catch (e: AuthorizationException) {
            return null
        } catch (e: PermissionsException) {
            return null
        } catch (e: Exception) {
            SecurityWebResourceFilter.log.debug("resolve token error!", e)
        }
        return null
    }

    fun getToken(request: HttpServletRequest): SecurityToken {
        val raw = request.getHeader(properties.getHeaderAuthorization())

        // 走参数
        if (!hasText(raw)) {
            return getTokenByParams(request)
        }

        return SecurityToken.ofDelimiter(raw!!, " ")
    }

    fun getTokenByParams(request: HttpServletRequest): SecurityToken {
        val value = request.getParameter(properties.getParamAuthorization())
        return SecurityToken.ofDelimiter(value, " ")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SecurityWebResourceFilter::class.java)
    }
}
