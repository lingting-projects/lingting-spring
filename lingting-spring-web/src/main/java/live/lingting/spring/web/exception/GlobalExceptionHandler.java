
package live.lingting.spring.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.framework.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
@Order(GlobalExceptionHandler.ORDER)
public class GlobalExceptionHandler {

	public static final int ORDER = -100;

	/**
	 * Business error
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BizException.class)
	public R<String> handle2BizException(HttpServletRequest request, BizException e) {
		log.error("uri: {}, Business error! code: {}; message: {};", request.getRequestURI(), e.getCode(),
				e.getMessage(), e.getCause());
		return R.failed(e.getCode(), e.getMessage());
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	public R<String> handleNullPointerException(HttpServletRequest request, NullPointerException e) {
		log.error("uri: {}, NullPointerException!", request.getRequestURI(), e);
		return R.failed(ApiResultCode.SERVER_ERROR);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public R<String> handleMethodArgumentTypeMismatchException(HttpServletRequest request, Exception e) {
		log.error("uri: {}, ArgumentTypeMismatchException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.SERVER_PARAM_CONVERT_ERROR);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(HttpServletRequest request, Exception e) {
		log.error("uri: {}, MethodNotSupportedException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.SERVER_METHOD_ERROR);
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public R<String> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
		log.error("uri: {}, IllegalArgumentException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.SERVER_PARAM_INVALID_ERROR);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler({ ValidationException.class, MethodArgumentNotValidException.class, BindException.class })
	public R<String> handleValidationException(HttpServletRequest request, Exception e) {
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
		log.error("uri: {}, ValidationException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.PARAMS_ERROR, message);
	}

	/**
	 * 参数类型转换异常
	 */
	@ExceptionHandler(ConversionFailedException.class)
	public R<String> handlerConversionFailedException(HttpServletRequest request, ConversionFailedException e) {
		log.error("uri: {}; ConversionFailedException!", request.getRequestURI(), e);
		return R.failed(ApiResultCode.PARAMS_ERROR);
	}

	/**
	 * 请求体异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public R<String> handleHttpBodyException(HttpServletRequest request, HttpMessageNotReadableException e) {
		log.error("uri: {}, HttpBodyException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.PARAM_BODY_ERROR);
	}

	/**
	 * 参数缺失异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public R<String> handleParamsMissingException(HttpServletRequest request,
			MissingServletRequestParameterException e) {
		log.error("uri: {}, ParamsMissingException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.PARAM_MISSING_ERROR);
	}

	/**
	 * 鉴权异常
	 */
	@ExceptionHandler(SecurityException.class)
	public R<String> handlerSecurityException(SecurityException e) {
		return R.failed(ApiResultCode.UNAUTHORIZED_ERROR, e.getMessage());
	}

	/**
	 * sql执行异常
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public R<String> handlerSQLIntegrityConstraintViolationException(HttpServletRequest request,
			SQLIntegrityConstraintViolationException e) {
		log.error("uri: {}; SQLIntegrityConstraintViolationException! {}", request.getRequestURI(), e.getMessage());
		return R.failed(ApiResultCode.DB_CONSTRAINT_VIOLATION_ERROR);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public R<String> handlerNoResourceFoundException() {
		return R.failed(ApiResultCode.NOT_FOUND);
	}

}
