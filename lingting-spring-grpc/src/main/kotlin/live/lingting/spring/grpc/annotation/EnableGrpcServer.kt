package live.lingting.spring.grpc.annotation

import java.lang.annotation.Inherited
import live.lingting.spring.grpc.configuration.GrpcServerConfiguration
import org.springframework.context.annotation.Import

/**
 * @author lingting 2024-02-05 16:05
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
@Import(GrpcServerConfiguration::class)
annotation class EnableGrpcServer
