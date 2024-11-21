package live.lingting.spring.elasticsearch

import live.lingting.framework.elasticsearch.datascope.DefaultElasticsearchDataPermissionHandler
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-03-08 18:06
 */
@Component
class SwitchElasticsearchDataPermissionHandler(scopes: MutableList<ElasticsearchDataScope>) : DefaultElasticsearchDataPermissionHandler(scopes) {
    /**
     * true 则不忽略权限控制
     */
    private var enabled = false

    override fun ignorePermissionControl(index: String): Boolean {
        return !enabled
    }

    fun enable() {
        this.enabled = true
    }

    fun disable() {
        this.enabled = false
    }
}
