package live.lingting.spring.security.conditional

import java.lang.annotation.Inherited
import live.lingting.spring.security.configuration.SecurityAuthorizationConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean

/**
 * @author lingting 2024-02-05 17:21
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@ConditionalOnBean(SecurityAuthorizationConfiguration::class)
annotation class ConditionalOnAuthorization
