package live.lingting.spring.elasticsearch;

import live.lingting.framework.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-03-08 14:44
 */
@ConfigurationProperties(ElasticsearchSpringProperties.PREFIX)
public class ElasticsearchSpringProperties {

	public static final String PREFIX = "lingting.elasticsearch";

	/**
	 * 重试配置
	 */
	@NestedConfigurationProperty
	protected ElasticsearchProperties.Retry retry = new ElasticsearchProperties.Retry();

	/**
	 * 滚动查询配置
	 */
	@NestedConfigurationProperty
	protected ElasticsearchProperties.Scroll scroll = new ElasticsearchProperties.Scroll();

	public ElasticsearchProperties properties() {
		ElasticsearchProperties properties = new ElasticsearchProperties();
		properties.setRetry(this.retry);
		properties.setScroll(this.scroll);
		return properties;
	}

	public ElasticsearchProperties.Retry getRetry() {
		return this.retry;
	}

	public ElasticsearchProperties.Scroll getScroll() {
		return this.scroll;
	}

	public void setRetry(ElasticsearchProperties.Retry retry) {
		this.retry = retry;
	}

	public void setScroll(ElasticsearchProperties.Scroll scroll) {
		this.scroll = scroll;
	}

}
