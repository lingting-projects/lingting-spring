package live.lingting.spring.elasticsearch

import live.lingting.framework.elasticsearch.ElasticsearchProperties
import live.lingting.framework.elasticsearch.ElasticsearchProperties.Scroll
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author lingting 2024-03-08 14:44
 */
@ConfigurationProperties(ElasticsearchSpringProperties.PREFIX)
class ElasticsearchSpringProperties {
    /**
     * 重试配置
     */
    @NestedConfigurationProperty
    var retry: ElasticsearchProperties.Retry = ElasticsearchProperties.Retry()

    /**
     * 滚动查询配置
     */
    @NestedConfigurationProperty
    var scroll: Scroll = Scroll()

    fun properties(): ElasticsearchProperties {
        val properties = ElasticsearchProperties()
        properties.retry = this.retry
        properties.scroll = this.scroll
        return properties
    }

    companion object {
        const val PREFIX: String = "lingting.elasticsearch"
    }
}
