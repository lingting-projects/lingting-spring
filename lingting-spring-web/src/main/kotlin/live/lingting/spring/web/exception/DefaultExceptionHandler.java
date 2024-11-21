package live.lingting.spring.web.exception;

import live.lingting.framework.api.ApiResultCode;
import live.lingting.framework.api.R;
import live.lingting.spring.web.scope.WebScopeHolder;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author lingting 2022/9/21 15:55
 */
@Order
@ControllerAdvice
public class DefaultExceptionHandler extends AbstractExceptionHandler {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(DefaultExceptionHandler.class);

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
