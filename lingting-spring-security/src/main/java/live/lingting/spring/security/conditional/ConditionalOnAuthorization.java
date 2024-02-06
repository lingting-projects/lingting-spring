package live.lingting.spring.security.conditional;

import live.lingting.spring.security.configuration.SecurityAuthorizationConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2024-02-05 17:21
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public @interface ConditionalOnAuthorization {

}
