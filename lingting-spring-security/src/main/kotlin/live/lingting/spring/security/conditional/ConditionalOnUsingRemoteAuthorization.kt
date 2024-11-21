package live.lingting.spring.security.conditional;

import live.lingting.spring.security.configuration.SecurityResourceConfiguration;
import live.lingting.spring.security.properties.SecuritySpringProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2024-02-05 17:21
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnBean(SecurityResourceConfiguration.class)
@ConditionalOnProperty(prefix = SecuritySpringProperties.PREFIX + ".authorization", value = "remote",
		havingValue = "true")
public @interface ConditionalOnUsingRemoteAuthorization {

}
