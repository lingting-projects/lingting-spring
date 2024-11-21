package live.lingting.spring.web.exception

import jakarta.validation.ValidationException
import java.sql.SQLIntegrityConstraintViolationException
import live.lingting.framework.api.ApiResultCode
import live.lingting.framework.api.R
import live.lingting.framework.exception.BizException
import live.lingting.spring.web.scope.WebScopeHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

/**
 * @author lingting 2022/9/21 15:55
 */
@ControllerAdvice
@Order(GlobalExceptionHandler.ORDER)
class GlobalExceptionHandler : AbstractExceptionHandler() {
    /**
     * Business error
     * @param e the e
     * @return R
     */
    @ExceptionHandler(BizException::class)
    fun handle2BizException(e: BizException): ResponseEntity<R<String>> {
        log.error(
            "uri: {}, Business error! code: {}; message: {};", WebScopeHolder.uri(), e.code, e.message,
            e.cause
        )
        return extract<R<String>>(R.failed<String>(e.code, e.message))
    }

    /**
     * 空指针异常捕获
     * @param e the e
     * @return R
     */
    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(e: NullPointerException): ResponseEntity<R<String>> {
        log.error("uri: {}, NullPointerException!", WebScopeHolder.uri(), e)
        return extract<R<String>>(R.failed<String>(ApiResultCode.SERVER_ERROR))
    }

    /**
     * MethodArgumentTypeMismatchException 参数类型转换异常
     * @param e the e
     * @return R
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: Exception): ResponseEntity<R<String>> {
        log.error("uri: {}, ArgumentTypeMismatchException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.SERVER_PARAM_CONVERT_ERROR))
    }

    /**
     * 请求方式有问题 - MediaType 异常 - Method 异常
     * @return R
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class, HttpRequestMethodNotSupportedException::class)
    fun requestNotSupportedException(e: Exception): ResponseEntity<R<String>> {
        log.error("uri: {}, MethodNotSupportedException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.SERVER_METHOD_ERROR))
    }

    /**
     * IllegalArgumentException 异常捕获，主要用于Assert
     * @param e the e
     * @return R
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<R<String>> {
        log.error("uri: {}, IllegalArgumentException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.SERVER_PARAM_INVALID_ERROR))
    }

    /**
     * 单体参数校验异常 validation Exception
     * @param e the e
     * @return R
     */
    @ExceptionHandler(ValidationException::class, MethodArgumentNotValidException::class, BindException::class)
    fun handleValidationException(e: Exception): ResponseEntity<R<String>> {
        var message = e.localizedMessage
        var fe: FieldError? = null
        if (e is MethodArgumentNotValidException) {
            fe = e.fieldError
        } else if (e is BindException) {
            fe = e.fieldError
        }

        if (fe != null) {
            message = fe.defaultMessage
        }
        log.error("uri: {}, ValidationException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.PARAMS_ERROR, message!!))
    }

    /**
     * 参数类型转换异常
     */
    @ExceptionHandler(ConversionFailedException::class)
    fun handlerConversionFailedException(e: ConversionFailedException): ResponseEntity<R<String>> {
        log.error("uri: {}; ConversionFailedException!", WebScopeHolder.uri(), e)
        return extract<R<String>>(R.failed<String>(ApiResultCode.PARAMS_ERROR))
    }

    /**
     * 请求体异常
     * @param e the e
     * @return R
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpBodyException(e: HttpMessageNotReadableException): ResponseEntity<R<String>> {
        log.error("uri: {}, HttpBodyException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.PARAM_BODY_ERROR))
    }

    /**
     * 参数缺失异常
     * @param e the e
     * @return R
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleParamsMissingException(e: MissingServletRequestParameterException): ResponseEntity<R<String>> {
        log.error("uri: {}, ParamsMissingException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.PARAM_MISSING_ERROR))
    }

    /**
     * 鉴权异常
     */
    @ExceptionHandler(SecurityException::class)
    fun handlerSecurityException(e: SecurityException): ResponseEntity<R<String>> {
        return extract<R<String>>(R.failed<String>(ApiResultCode.UNAUTHORIZED_ERROR, e.message!!))
    }

    /**
     * sql执行异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun handlerSQLIntegrityConstraintViolationException(
        e: SQLIntegrityConstraintViolationException
    ): ResponseEntity<R<String>> {
        log.error("uri: {}; SQLIntegrityConstraintViolationException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.DB_CONSTRAINT_VIOLATION_ERROR))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handlerNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<R<String>> {
        log.warn("uri: {}, NoResourceFoundException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.NOT_FOUND))
    }

    /**
     * 404异常. 需要配合两个设置
     *
     *
     * 1. spring.mvc.throw-exception-if-no-handler-found=true
     *
     *
     *
     * 2. spring.web.resources.add-mappings=false
     *
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handlerNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<R<String>> {
        log.warn("uri: {}, NoHandlerFoundException! {}", WebScopeHolder.uri(), e.message)
        return extract<R<String>>(R.failed<String>(ApiResultCode.NOT_FOUND_ERROR))
    }

    companion object {
        const val ORDER: Int = -100

        private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
