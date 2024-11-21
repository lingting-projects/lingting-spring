package live.lingting.spring.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author lingting 2024-03-29 11:10
 */
public abstract class AbstractExceptionHandler {

	public <T> ResponseEntity<T> extract(T t) {
		return extract(t, HttpStatus.OK);
	}

	public <T> ResponseEntity<T> extract(T t, HttpStatus status) {
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(t);
	}

}
