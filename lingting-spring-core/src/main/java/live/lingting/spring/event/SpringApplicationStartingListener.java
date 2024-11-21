package live.lingting.spring.event;

import live.lingting.framework.context.ContextHolder;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author lingting 2023-12-06 17:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SpringApplicationStartingListener.class);

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		log.debug("spring application starting");
		ContextHolder.start();
	}

}
