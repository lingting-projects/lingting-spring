package live.lingting.spring.security.web.endpoint;

import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2024-03-22 10:06
 */
public class SecurityWebEndpointHandlerMapping extends RequestMappingHandlerMapping {

	/**
	 * @see WebMvcAutoConfiguration.EnableWebMvcConfiguration#mvcConversionService()
	 * @see WebMvcConfigurationSupport#getInterceptors
	 */
	public SecurityWebEndpointHandlerMapping(WebConversionService conversionService,
			List<HandlerInterceptor> interceptors) {
		setOrder(Ordered.LOWEST_PRECEDENCE - 100);
		HandlerInterceptor[] handlerInterceptors = getInterceptors(conversionService, interceptors);
		setInterceptors((Object[]) handlerInterceptors);
	}

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return SecurityWebEndpoint.class.isAssignableFrom(beanType);
	}

	HandlerInterceptor[] getInterceptors(WebConversionService conversionService,
			List<HandlerInterceptor> interceptors) {
		List<HandlerInterceptor> list = new ArrayList<>(interceptors.size() + 1);
		list.add(new ConversionServiceExposingInterceptor(conversionService));
		list.addAll(interceptors);
		return list.toArray(new HandlerInterceptor[0]);
	}

}
