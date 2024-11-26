package live.lingting.spring.actuator.configuration

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import live.lingting.framework.data.DataSize
import live.lingting.spring.actuator.MeterRegisterHandler
import live.lingting.spring.actuator.health.DiskSpaceReadableHealthIndicator
import live.lingting.spring.actuator.health.ThreadPoolHealthIndicator
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthContributorAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthIndicatorProperties
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2023-07-25 17:29
 */
@AutoConfiguration(before = [DiskSpaceHealthContributorAutoConfiguration::class])
open class ActuatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MeterRegistry::class)
    open fun meterRegisterHandler(registry: MeterRegistry, binders: MutableList<MeterBinder>): MeterRegisterHandler {
        return MeterRegisterHandler(registry, binders)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun threadPoolHealthIndicator(): ThreadPoolHealthIndicator {
        return ThreadPoolHealthIndicator()
    }

    @Bean
    @ConditionalOnEnabledHealthIndicator("diskspace")
    @ConditionalOnMissingBean(name = ["diskSpaceHealthIndicator"])
    open fun diskSpaceHealthIndicator(properties: DiskSpaceHealthIndicatorProperties): DiskSpaceHealthIndicator {
        val threshold = properties.threshold
        return DiskSpaceReadableHealthIndicator(properties.path, DataSize.ofBytes(threshold.toBytes()))
    }
}
