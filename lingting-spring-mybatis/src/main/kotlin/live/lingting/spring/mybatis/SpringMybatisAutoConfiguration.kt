package live.lingting.spring.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import live.lingting.framework.datascope.JsqlDataScope;
import live.lingting.framework.datascope.handler.DataPermissionHandler;
import live.lingting.framework.datascope.handler.DefaultDataPermissionHandler;
import live.lingting.framework.datascope.parser.DataScopeParser;
import live.lingting.framework.datascope.parser.DefaultDataScopeParser;
import live.lingting.framework.mybatis.datascope.DataPermissionInterceptor;
import live.lingting.framework.mybatis.typehandler.AutoRegisterTypeHandler;
import live.lingting.spring.jackson.ObjectMapperAfter;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-03-11 10:49
 */
@AutoConfiguration
public class SpringMybatisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DataScopeParser dataScopeParser() {
		return new DefaultDataScopeParser();
	}

	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<JsqlDataScope> scopes) {
		return new DefaultDataPermissionHandler(scopes);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataPermissionInterceptor dataPermissionInterceptor(DataScopeParser parser, DataPermissionHandler handler) {
		return new DataPermissionInterceptor(parser, handler);
	}

	/**
	 * MybatisPlusInterceptor 插件，默认提供分页插件</br>
	 * 如需其他MP内置插件，则需自定义该Bean
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	@Bean
	public ObjectMapperAfter mybatisObjectMapperAfter() {
		return JacksonTypeHandler::setObjectMapper;
	}

	@Bean
	@ConditionalOnMissingBean
	public ExtendServiceImplBeanPost extendServiceImplBeanPost() {
		return new ExtendServiceImplBeanPost();
	}

	@Bean
	public ConfigurationCustomizer autoRegisterCOnfigurationCustomizer(List<AutoRegisterTypeHandler<?>> list) {
		return configuration -> {
			TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
			for (AutoRegisterTypeHandler<?> handler : list) {
				registry.register(handler);
			}
		};
	}

}
