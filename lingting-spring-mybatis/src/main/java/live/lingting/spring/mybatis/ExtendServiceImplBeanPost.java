package live.lingting.spring.mybatis;

import live.lingting.framework.mybatis.extend.ExtendMapper;
import live.lingting.framework.mybatis.extend.ExtendServiceImpl;
import live.lingting.spring.post.SpringBeanPostProcessor;
import live.lingting.spring.util.SpringUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2024-03-12 17:41
 */
@RequiredArgsConstructor
@SuppressWarnings("java:S3740")
public class ExtendServiceImplBeanPost implements SpringBeanPostProcessor {

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean instanceof ExtendServiceImpl;
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		if (bean instanceof ExtendServiceImpl impl) {
			Class mapperClass = impl.getMapperClass();
			Object mapper = SpringUtils.getBean(mapperClass);
			impl.setMapper((ExtendMapper) mapper);
		}
		return bean;
	}

}
