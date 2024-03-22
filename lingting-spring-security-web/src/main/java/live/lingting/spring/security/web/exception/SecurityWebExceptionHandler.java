package live.lingting.spring.security.web.exception;

import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.framework.security.exception.AuthorizationException;
import live.lingting.framework.security.exception.PermissionsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityWebExceptionHandler {

	/**
	 * 鉴权异常
	 */
	@ExceptionHandler(AuthorizationException.class)
	public R<String> handlerAuthorizationException(AuthorizationException e) {
		log.error("AuthorizationException! {}", e.getMessage());
		return R.failed(ApiResultCode.UNAUTHORIZED_ERROR);
	}

	/**
	 * 权限异常
	 */
	@ExceptionHandler(PermissionsException.class)
	public R<String> handlerPermissionsException(PermissionsException e) {
		log.error("PermissionsException! {}", e.getMessage());
		return R.failed(ApiResultCode.FORBIDDEN_ERROR);
	}

}
