package live.lingting.spring.web.annotation;

import live.lingting.spring.web.exception.DefaultExceptionHandler;
import live.lingting.spring.web.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2024-03-20 15:40
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({ DefaultExceptionHandler.class, GlobalExceptionHandler.class })
public @interface EnableExceptionHandler {

}
