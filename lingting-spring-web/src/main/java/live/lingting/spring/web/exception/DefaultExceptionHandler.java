package live.lingting.spring.web.exception;

import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.spring.web.scope.WebScopeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@Order
@ControllerAdvice
public class DefaultExceptionHandler extends AbstractExceptionHandler {

	/**
	 * 其他异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<R<String>> handleException(Exception e) {
		log.error("uri: {}, unknown error!", WebScopeHolder.uri(), e);
		return extract(R.failed(ApiResultCode.SERVER_ERROR));
	}

}
