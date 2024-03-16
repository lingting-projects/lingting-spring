package live.lingting.spring.datascope.configuration;

import live.lingting.framework.datascope.JsqlDataScope;
import live.lingting.framework.datascope.handler.DataPermissionHandler;
import live.lingting.framework.datascope.handler.DefaultDataPermissionHandler;
import live.lingting.framework.datascope.parser.DataScopeParser;
import live.lingting.framework.datascope.parser.DefaultDataScopeParser;
import live.lingting.framework.util.ClassUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-02-02 16:40
 */
@AutoConfiguration
public class DataScopeAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<JsqlDataScope> scopes) {
		return new DefaultDataPermissionHandler(scopes);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataScopeParser dataScopeParser() {
		// 如果使用底本jsql
		if (ClassUtils.isPresent("net.sf.jsqlparser.statement.select.SelectBody",
				DataScopeParser.class.getClassLoader())) {
			return new JsqlLowerDataScopeParser();
		}
		return new DefaultDataScopeParser();
	}

}
