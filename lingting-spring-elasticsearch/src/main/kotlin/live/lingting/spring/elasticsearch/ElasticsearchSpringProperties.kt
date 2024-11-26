package live.lingting.spring.elasticsearch

import live.lingting.framework.elasticsearch.ElasticsearchProperties
import live.lingting.framework.elasticsearch.ElasticsearchProperties.Index
import live.lingting.framework.elasticsearch.ElasticsearchProperties.Scroll
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author lingting 2024-03-08 14:44
 */
@ConfigurationProperties(ElasticsearchSpringProperties.PREFIX)
class ElasticsearchSpringProperties {

    companion object {
        const val PREFIX: String = "lingting.elasticsearch"
    }

    /**
     * 索引设置
     */
    @NestedConfigurationProperty
    var index: Index = Index()

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
        properties.index = index
        properties.retry = retry
        properties.scroll = scroll
        return properties
    }

}
