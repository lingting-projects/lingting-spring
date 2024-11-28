package live.lingting.spring.security.web.resource

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.domain.SecurityScope
import live.lingting.framework.security.domain.SecurityToken
import live.lingting.framework.security.exception.AuthorizationException
import live.lingting.framework.security.exception.PermissionsException
import live.lingting.framework.security.resource.SecurityResourceService
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.security.web.properties.SecurityWebProperties
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author lingting 2024-03-21 19:49
 */
open class SecurityWebResourceFilter(
    val properties: SecurityWebProperties,
    val service: SecurityResourceService,
    val convert: SecurityConvert,
) : OncePerRequestFilter() {

    companion object {
        private val log = logger()
    }

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

    protected fun getScope(request: HttpServletRequest): SecurityScope? {
        val token = getToken(request)
        // token有效, 设置上下文
        if (!token.isAvailable) {
            return null
        }
        try {
            return service.resolve(token)
        } catch (_: AuthorizationException) {
            return null
        } catch (_: PermissionsException) {
            return null
        } catch (e: Exception) {
            log.debug("resolve token error!", e)
        }
        return null
    }

    fun getToken(request: HttpServletRequest): SecurityToken {
        val rawByHeader = request.getHeader(properties.headerAuthorization)

        if (!rawByHeader.isNullOrBlank()) {
            return convert.toToken(rawByHeader)
        }

        // 走参数
        val rawByParams = request.getParameter(properties.paramAuthorization)
        return convert.toToken(rawByParams)
    }

}
