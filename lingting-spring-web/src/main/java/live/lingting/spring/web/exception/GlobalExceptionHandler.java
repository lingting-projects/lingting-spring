
package live.lingting.spring.web.exception;

import jakarta.validation.ValidationException;
import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.framework.exception.BizException;
import live.lingting.spring.web.scope.WebScopeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@ControllerAdvice
@Order(GlobalExceptionHandler.ORDER)
public class GlobalExceptionHandler extends AbstractExceptionHandler {

	public static final int ORDER = -100;

	/**
	 * Business error
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BizException.class)
	public ResponseEntity<R<String>> handle2BizException(BizException e) {
		log.error("uri: {}, Business error! code: {}; message: {};", WebScopeHolder.uri(), e.getCode(), e.getMessage(),
				e.getCause());
		return extract(R.failed(e.getCode(), e.getMessage()));
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<R<String>> handleNullPointerException(NullPointerException e) {
		log.error("uri: {}, NullPointerException!", WebScopeHolder.uri(), e);
		return extract(R.failed(ApiResultCode.SERVER_ERROR));
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<R<String>> handleMethodArgumentTypeMismatchException(Exception e) {
		log.error("uri: {}, ArgumentTypeMismatchException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.SERVER_PARAM_CONVERT_ERROR));
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<R<String>> requestNotSupportedException(Exception e) {
		log.error("uri: {}, MethodNotSupportedException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.SERVER_METHOD_ERROR));
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<R<String>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("uri: {}, IllegalArgumentException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.SERVER_PARAM_INVALID_ERROR));
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler({ ValidationException.class, MethodArgumentNotValidException.class, BindException.class })
	public ResponseEntity<R<String>> handleValidationException(Exception e) {
		String message = e.getLocalizedMessage();
		FieldError fe = null;
		if (e instanceof MethodArgumentNotValidException me) {
			fe = me.getFieldError();
		}
		else if (e instanceof BindException be) {
			fe = be.getFieldError();
		}

		if (fe != null) {
			message = fe.getDefaultMessage();
		}
		log.error("uri: {}, ValidationException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.PARAMS_ERROR, message));
	}

	/**
	 * 参数类型转换异常
	 */
	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<R<String>> handlerConversionFailedException(ConversionFailedException e) {
		log.error("uri: {}; ConversionFailedException!", WebScopeHolder.uri(), e);
		return extract(R.failed(ApiResultCode.PARAMS_ERROR));
	}

	/**
	 * 请求体异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<R<String>> handleHttpBodyException(HttpMessageNotReadableException e) {
		log.error("uri: {}, HttpBodyException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.PARAM_BODY_ERROR));
	}

	/**
	 * 参数缺失异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<R<String>> handleParamsMissingException(MissingServletRequestParameterException e) {
		log.error("uri: {}, ParamsMissingException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.PARAM_MISSING_ERROR));
	}

	/**
	 * 鉴权异常
	 */
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<R<String>> handlerSecurityException(SecurityException e) {
		return extract(R.failed(ApiResultCode.UNAUTHORIZED_ERROR, e.getMessage()));
	}

	/**
	 * sql执行异常
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<R<String>> handlerSQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException e) {
		log.error("uri: {}; SQLIntegrityConstraintViolationException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.DB_CONSTRAINT_VIOLATION_ERROR));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<R<String>> handlerNoResourceFoundException(NoResourceFoundException e) {
		log.warn("uri: {}, NoResourceFoundException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.NOT_FOUND));
	}

	/**
	 * 404异常. 需要配合两个设置
	 * <p>
	 * 1. spring.mvc.throw-exception-if-no-handler-found=true
	 * </p>
	 * <p>
	 * 2. spring.web.resources.add-mappings=false
	 * </p>
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<R<String>> handlerNoHandlerFoundException(NoHandlerFoundException e) {
		log.warn("uri: {}, NoHandlerFoundException! {}", WebScopeHolder.uri(), e.getMessage());
		return extract(R.failed(ApiResultCode.NOT_FOUND_ERROR));
	}

}
