package live.lingting.spring.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author lingting 2023-07-25 18:30
 */
public class MeterRegisterHandler implements InitializingBean {

	private final MeterRegistry registry;

	private final List<MeterBinder> binders;

	public MeterRegisterHandler(MeterRegistry registry, List<MeterBinder> binders) {
		this.registry = registry;
		this.binders = binders;
	}

	public void registryAll() {
		for (MeterBinder binder : binders) {
			binder.bindTo(registry);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		registryAll();
	}

}
