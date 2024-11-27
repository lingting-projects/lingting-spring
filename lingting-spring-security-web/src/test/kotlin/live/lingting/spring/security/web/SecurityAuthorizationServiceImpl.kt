package live.lingting.spring.security.web

import live.lingting.framework.security.authorize.SecurityAuthorizationService
import live.lingting.framework.security.domain.SecurityScope
import live.lingting.framework.security.domain.SecurityScopeAttributes
import live.lingting.framework.security.domain.SecurityToken
import live.lingting.framework.security.exception.AuthorizationException
import live.lingting.framework.security.store.SecurityStore
import live.lingting.framework.time.DateTime
import live.lingting.framework.util.LocalDateTimeUtils.timestamp
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-03-21 20:23
 */
@Component
class SecurityAuthorizationServiceImpl(private val store: SecurityStore) : SecurityAuthorizationService {

    override fun validAndBuildScope(username: String?, password: String?): SecurityScope? {
        username!!
        if (username == password) {
            val scope = SecurityScope()
            scope.authorization = username
            scope.tenantId = username
            scope.userId = username
            scope.username = username
            scope.nickname = username
            scope.password = password
            scope.avatar = ""
            scope.enabled = true
            scope.expireTime = DateTime.current().plusMonths(3).timestamp
            scope.roles = mutableSetOf<String>(username)
            scope.permissions = mutableSetOf<String>(username)
            scope.attributes = SecurityScopeAttributes()
            return scope
        }
        throw AuthorizationException("用户名或密码错误!")
    }

    override fun refresh(token: SecurityToken): SecurityScope? {
        val scope = store.get(token)
        if (scope != null) {
            scope.expireTime = System.currentTimeMillis() * 2
        }
        return scope
    }
}
