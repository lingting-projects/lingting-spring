package live.lingting.spring.security.web.endpoint

import live.lingting.framework.security.annotation.Authorize
import live.lingting.framework.security.authorize.SecurityAuthorizationService
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.domain.AuthorizationPasswordPO
import live.lingting.framework.security.domain.AuthorizationVO
import live.lingting.framework.security.exception.AuthorizationException
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.framework.security.resource.SecurityHolder.scope
import live.lingting.framework.security.resource.SecurityHolder.token
import live.lingting.framework.security.store.SecurityStore
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author lingting 2024-03-21 19:31
 */
@Authorize
@ResponseBody
class SecurityWebEndpoint(
    private val service: SecurityAuthorizationService, private val store: SecurityStore,
    private val securityPassword: SecurityPassword, private val convert: SecurityConvert
) {
    @DeleteMapping("authorization/logout")
    fun logout(): AuthorizationVO {
        val scope = scope()
        store.deleted(scope!!)
        return convert.scopeToVo(scope)
    }

    @Authorize(anyone = true)
    @GetMapping("authorization/password")
    fun password(po: AuthorizationPasswordPO): AuthorizationVO {
        val username = po.username
        val rawPassword = po.password
        val password = securityPassword.decodeFront(rawPassword!!)
        val scope = service.validAndBuildScope(username, password)
        if (scope == null) {
            throw AuthorizationException("Username or password is incorrect!")
        }
        store.save(scope)
        return convert.scopeToVo(scope)
    }

    @GetMapping("authorization/refresh")
    fun refresh(): AuthorizationVO {
        val scope = service.refresh(token())
        if (scope == null) {
            throw AuthorizationException("Login authorization has expired!")
        }
        store.update(scope)
        return convert.scopeToVo(scope)
    }

    @GetMapping("authorization/resolve")
    fun resolve(): AuthorizationVO {
        val scope = scope()
        return convert.scopeToVo(scope)
    }
}
