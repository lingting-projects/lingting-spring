package live.lingting.spring.security.grpc.exception;

import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.framework.grpc.exception.GrpcExceptionHandler;
import live.lingting.framework.grpc.exception.GrpcExceptionInstance;
import live.lingting.framework.security.exception.AuthorizationException;
import live.lingting.framework.security.exception.PermissionsException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2024-03-27 10:19
 */
@Slf4j
public class SecurityExceptionInstance implements GrpcExceptionInstance {

	/**
	 * 鉴权异常
	 */
	@GrpcExceptionHandler(AuthorizationException.class)
	public R<String> handlerAuthorizationException(AuthorizationException e) {
		log.error("AuthorizationException! {}", e.getMessage());
		return R.failed(ApiResultCode.UNAUTHORIZED_ERROR);
	}

	/**
	 * 权限异常
	 */
	@GrpcExceptionHandler(PermissionsException.class)
	public R<String> handlerPermissionsException(PermissionsException e) {
		log.error("PermissionsException! {}", e.getMessage());
		return R.failed(ApiResultCode.FORBIDDEN_ERROR);
	}

}
