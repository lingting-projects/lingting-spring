package live.lingting.spring.security.web;

import live.lingting.framework.security.authorize.SecurityAuthorizationService;
import live.lingting.framework.security.domain.SecurityScope;
import live.lingting.framework.security.domain.SecurityScopeAttributes;
import live.lingting.framework.security.exception.AuthorizationException;
import live.lingting.framework.security.store.SecurityStore;
import live.lingting.framework.util.LocalDateTimeUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * @author lingting 2024-03-21 20:23
 */
@Component
public class SecurityAuthorizationServiceImpl implements SecurityAuthorizationService {

	private final SecurityStore store;

	public SecurityAuthorizationServiceImpl(SecurityStore store) {
		this.store = store;
	}

	@Override
	public SecurityScope validAndBuildScope(String username, String password) throws AuthorizationException {
		if (Objects.equals(username, password)) {
			SecurityScope scope = new SecurityScope();
			scope.setToken(username);
			scope.setTenantId(username);
			scope.setUserId(username);
			scope.setUsername(username);
			scope.setNickname(username);
			scope.setPassword(password);
			scope.setAvatar("");
			scope.setEnabled(true);
			scope.setExpireTime(LocalDateTimeUtils.toTimestamp(LocalDateTime.now().plusMonths(3)));
			scope.setRoles(Collections.singleton(username));
			scope.setPermissions(Collections.singleton(username));
			scope.setAttributes(new SecurityScopeAttributes());
			return scope;
		}
		throw new AuthorizationException("用户名或密码错误!");
	}

	@Override
	public SecurityScope refresh(String token) {
		SecurityScope scope = store.get(token);
		scope.setExpireTime(System.currentTimeMillis() * 2);
		return scope;
	}

}
