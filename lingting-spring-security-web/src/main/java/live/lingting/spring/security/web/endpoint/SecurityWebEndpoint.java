package live.lingting.spring.security.web.endpoint;

import live.lingting.framework.security.annotation.Authorize;
import live.lingting.framework.security.authorize.SecurityAuthorizationService;
import live.lingting.framework.security.convert.SecurityConvert;
import live.lingting.framework.security.domain.AuthorizationPasswordPO;
import live.lingting.framework.security.domain.AuthorizationVO;
import live.lingting.framework.security.domain.SecurityScope;
import live.lingting.framework.security.exception.AuthorizationException;
import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.security.resource.SecurityHolder;
import live.lingting.framework.security.store.SecurityStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lingting 2024-03-21 19:31
 */
@Authorize
@ResponseBody
@RequiredArgsConstructor
public class SecurityWebEndpoint {

	private final SecurityAuthorizationService service;

	private final SecurityStore store;

	private final SecurityPassword securityPassword;

	private final SecurityConvert convert;

	@DeleteMapping("authorization/logout")
	public AuthorizationVO logout() {
		SecurityScope scope = SecurityHolder.scope();
		store.deleted(scope);
		return convert.scopeToVo(scope);
	}

	@Authorize(anyone = true)
	@GetMapping("authorization/password")
	public AuthorizationVO password(AuthorizationPasswordPO po) {
		String username = po.getUsername();
		String rawPassword = po.getPassword();
		String password = securityPassword.decodeFront(rawPassword);
		SecurityScope scope = service.validAndBuildScope(username, password);
		if (scope == null) {
			throw new AuthorizationException("Username or password is incorrect!");
		}
		store.save(scope);
		return convert.scopeToVo(scope);
	}

	@GetMapping("authorization/refresh")
	public AuthorizationVO refresh() {
		SecurityScope scope = service.refresh(SecurityHolder.token());
		if (scope == null) {
			throw new AuthorizationException("Login authorization has expired!");
		}
		store.update(scope);
		return convert.scopeToVo(scope);
	}

	@GetMapping("authorization/resolve")
	public AuthorizationVO resolve() {
		SecurityScope scope = SecurityHolder.scope();
		return convert.scopeToVo(scope);
	}

}
