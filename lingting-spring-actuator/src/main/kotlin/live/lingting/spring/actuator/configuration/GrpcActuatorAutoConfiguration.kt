package live.lingting.spring.actuator.configuration

import live.lingting.framework.grpc.GrpcServer
import live.lingting.spring.actuator.health.GrpcServerHealthIndicator
import live.lingting.spring.grpc.configuration.GrpcAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2023-07-25 17:29
 */
@ConditionalOnClass(GrpcServer::class, GrpcAutoConfiguration::class)
@AutoConfiguration(
    before = [ActuatorAutoConfiguration::class],
    after = [GrpcAutoConfiguration::class]
)
open class GrpcActuatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(GrpcServer::class)
    @ConditionalOnEnabledHealthIndicator("grpc")
    open fun grpcServerHealthIndicator(server: GrpcServer): GrpcServerHealthIndicator {
        return GrpcServerHealthIndicator(server)
    }

}
