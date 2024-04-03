package live.lingting.spring.post;

import live.lingting.framework.context.ContextComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2022/10/22 15:10
 */
@Slf4j
public class ContextComposeBeanPostProcessor implements SpringBeanPostProcessor {

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
