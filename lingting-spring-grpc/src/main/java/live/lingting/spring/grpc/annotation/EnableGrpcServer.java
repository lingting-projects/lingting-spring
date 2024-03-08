package live.lingting.spring.grpc.annotation;

import live.lingting.spring.grpc.configuration.GrpcServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2024-02-05 16:05
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(GrpcServerConfiguration.class)
public @interface EnableGrpcServer {

}
