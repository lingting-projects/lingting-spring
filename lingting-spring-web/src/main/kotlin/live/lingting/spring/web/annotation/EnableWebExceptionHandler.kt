package live.lingting.spring.web.annotation

import java.lang.annotation.Inherited
import live.lingting.spring.web.exception.DefaultExceptionHandler
import live.lingting.spring.web.exception.GlobalExceptionHandler
import org.springframework.context.annotation.Import

/**
 * @author lingting 2024-03-20 15:40
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
@Import(DefaultExceptionHandler::class, GlobalExceptionHandler::class)
annotation class EnableWebExceptionHandler
