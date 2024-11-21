package live.lingting.spring.security.annotation

import java.lang.annotation.Inherited
import live.lingting.spring.security.configuration.SecurityResourceConfiguration
import org.springframework.context.annotation.Import

/**
 * @author lingting 2023-03-29 21:08
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
@Import(SecurityResourceConfiguration::class)
annotation class EnableResourceServer
