package live.lingting.spring.actuator.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import live.lingting.spring.actuator.MeterRegisterHandler;
import live.lingting.spring.actuator.health.DiskSpaceReadableHealthIndicator;
import live.lingting.spring.actuator.health.ThreadPoolHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2023-07-25 17:29
 */
@AutoConfiguration(before = DiskSpaceHealthContributorAutoConfiguration.class)
public class ActuatorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(MeterRegistry.class)
	public MeterRegisterHandler meterRegisterHandler(MeterRegistry registry, List<MeterBinder> binders) {
		return new MeterRegisterHandler(registry, binders);
	}

	@Bean
	@ConditionalOnMissingBean
	public ThreadPoolHealthIndicator threadPoolHealthIndicator() {
		return new ThreadPoolHealthIndicator();
	}

	@Bean
	@ConditionalOnEnabledHealthIndicator("diskspace")
	@ConditionalOnMissingBean(name = "diskSpaceHealthIndicator")
	public DiskSpaceHealthIndicator diskSpaceHealthIndicator(DiskSpaceHealthIndicatorProperties properties) {
		return new DiskSpaceReadableHealthIndicator(properties.getPath(), properties.getThreshold());
	}

}
