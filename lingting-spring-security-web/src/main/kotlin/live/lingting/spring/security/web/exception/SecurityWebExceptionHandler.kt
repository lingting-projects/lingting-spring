package live.lingting.spring.security.web.exception

import live.lingting.framework.api.ApiResultCode
import live.lingting.framework.api.R
import live.lingting.framework.security.exception.AuthorizationException
import live.lingting.framework.security.exception.PermissionsException
import live.lingting.spring.web.exception.AbstractExceptionHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * @author lingting 2022/9/21 15:55
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class SecurityWebExceptionHandler : AbstractExceptionHandler() {
    /**
     * 鉴权异常
     */
    @ExceptionHandler(AuthorizationException::class)
    fun handlerAuthorizationException(e: AuthorizationException): ResponseEntity<R<String>> {
        SecurityWebExceptionHandler.log.error("AuthorizationException! {}", e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.UNAUTHORIZED_ERROR))
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(PermissionsException::class)
    fun handlerPermissionsException(e: PermissionsException): ResponseEntity<R<String>> {
        SecurityWebExceptionHandler.log.error("PermissionsException! {}", e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.FORBIDDEN_ERROR))
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SecurityWebExceptionHandler::class.java)
    }
}
