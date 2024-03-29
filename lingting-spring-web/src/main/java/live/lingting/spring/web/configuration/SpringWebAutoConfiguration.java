package live.lingting.spring.web.configuration;

import jakarta.servlet.DispatcherType;
import live.lingting.spring.web.filter.WebScopeFilter;
import live.lingting.spring.web.properties.SpringWebProperties;
import live.lingting.spring.web.resolve.ApiPaginationParamsResolve;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.EnumSet;
import java.util.List;

/**
 * @author lingting 2024-03-20 14:59
 */
@AutoConfiguration
@EnableConfigurationProperties(SpringWebProperties.class)
public class SpringWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingFilterBean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public FilterRegistrationBean<WebScopeFilter> webScopeFilter(SpringWebProperties properties) {
		WebScopeFilter filter = new WebScopeFilter(properties);
		FilterRegistrationBean<WebScopeFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		bean.setOrder(properties.getScopeFilterOrder());
		return bean;
	}

	@Bean
	@ConditionalOnMissingBean
	public ApiPaginationParamsResolve apiPaginationParamsResolve(SpringWebProperties properties) {
		return new ApiPaginationParamsResolve(properties.getPagination());
	}

	@Bean
	@ConditionalOnBean(ApiPaginationParamsResolve.class)
	public WebMvcConfigurer webMvcConfigurer(ApiPaginationParamsResolve resolve) {
		return new WebMvcConfigurer() {
			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				resolvers.add(resolve);
			}
		};
	}

}
