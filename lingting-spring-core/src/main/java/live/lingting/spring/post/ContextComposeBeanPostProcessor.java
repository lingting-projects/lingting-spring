package live.lingting.spring.post;

import live.lingting.framework.context.ContextComponent;
import org.slf4j.Logger;

/**
 * @author lingting 2022/10/22 15:10
 */
public class ContextComposeBeanPostProcessor implements SpringBeanPostProcessor {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ContextComposeBeanPostProcessor.class);

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean instanceof ContextComponent;
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		log.trace("class [{}] start.", bean.getClass());
		((ContextComponent) bean).onApplicationStart();
		return bean;
	}

}
