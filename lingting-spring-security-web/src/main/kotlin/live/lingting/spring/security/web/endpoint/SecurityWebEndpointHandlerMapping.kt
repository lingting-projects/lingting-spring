package live.lingting.spring.security.web.endpoint

import org.springframework.boot.autoconfigure.web.format.WebConversionService
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 * @author lingting 2024-03-22 10:06
 */
class SecurityWebEndpointHandlerMapping(
    conversionService: WebConversionService,
    interceptors: MutableList<HandlerInterceptor>
) : RequestMappingHandlerMapping() {
    /**
     * @see WebMvcAutoConfiguration.EnableWebMvcConfiguration.mvcConversionService
     * @see WebMvcConfigurationSupport.getInterceptors
     */
    init {
        setOrder(LOWEST_PRECEDENCE - 100)
        val handlerInterceptors = getInterceptors(conversionService, interceptors)
        setInterceptors(*handlerInterceptors as Array<Any>)
    }

    override fun isHandler(beanType: Class<*>): Boolean {
        return SecurityWebEndpoint::class.java.isAssignableFrom(beanType)
    }

    fun getInterceptors(
        conversionService: WebConversionService,
        interceptors: MutableList<HandlerInterceptor>
    ): Array<HandlerInterceptor> {
        val list: MutableList<HandlerInterceptor> = ArrayList<HandlerInterceptor>(interceptors.size + 1)
        list.add(ConversionServiceExposingInterceptor(conversionService))
        list.addAll(interceptors)
        return list.toTypedArray<HandlerInterceptor>()
    }
}
