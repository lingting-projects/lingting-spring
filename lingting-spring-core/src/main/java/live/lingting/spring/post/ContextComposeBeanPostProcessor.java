package live.lingting.spring.post;

import live.lingting.framework.context.ContextComponent;

/**
 * @author lingting 2022/10/22 15:10
 */
public class ContextComposeBeanPostProcessor implements SpringBeanPostProcessor {

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean instanceof ContextComponent;
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		((ContextComponent) bean).onApplicationStart();
		return bean;
	}

}
