package live.lingting.spring.web.exception

import live.lingting.framework.api.ApiResultCode
import live.lingting.framework.api.R
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.spring.web.scope.WebScopeHolder
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * @author lingting 2022/9/21 15:55
 */
@Order
@ControllerAdvice
class DefaultExceptionHandler : AbstractExceptionHandler() {
    /**
     * 其他异常
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<R<String>> {
        log.error("uri: {}, unknown error!", WebScopeHolder.uri(), e)
        return extract<R<String>>(R.failed<String>(ApiResultCode.SERVER_ERROR))
    }

    companion object {
        private val log = logger()
    }
}
