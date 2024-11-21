package live.lingting.spring.security.conditional

import java.lang.annotation.Inherited
import live.lingting.spring.security.configuration.SecurityResourceConfiguration
import live.lingting.spring.security.properties.SecuritySpringProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

/**
 * @author lingting 2024-02-05 17:21
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@ConditionalOnBean(SecurityResourceConfiguration::class)
@ConditionalOnProperty(prefix = SecuritySpringProperties.PREFIX + ".authorization", value = ["remote"], havingValue = "true")
annotation class ConditionalOnUsingRemoteAuthorization
