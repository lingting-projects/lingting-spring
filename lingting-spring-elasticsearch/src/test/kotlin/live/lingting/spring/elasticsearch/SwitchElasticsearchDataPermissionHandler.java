package live.lingting.spring.elasticsearch;

import live.lingting.framework.elasticsearch.datascope.DefaultElasticsearchDataPermissionHandler;
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lingting 2024-03-08 18:06
 */
@Component
public class SwitchElasticsearchDataPermissionHandler extends DefaultElasticsearchDataPermissionHandler {

	/**
	 * true 则不忽略权限控制
	 */
	private boolean enabled = false;

	public SwitchElasticsearchDataPermissionHandler(List<ElasticsearchDataScope> scopes) {
		super(scopes);
	}

	@Override
	public boolean ignorePermissionControl(String index) {
		return !enabled;
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}

}
