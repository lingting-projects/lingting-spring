package live.lingting.spring.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@Order
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class DefaultExceptionHandler {

	/**
	 * 其他异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	public R<String> handleException(Exception e, HttpServletRequest request) {
		log.error("uri: {}, unknown error!", request.getRequestURI(), e);
		return R.failed(ApiResultCode.SERVER_ERROR);
	}

}
