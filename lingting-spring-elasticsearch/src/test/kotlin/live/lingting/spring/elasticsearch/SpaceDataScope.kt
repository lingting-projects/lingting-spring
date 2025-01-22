package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch._types.query_dsl.Query
import live.lingting.framework.elasticsearch.IndexInfo
import live.lingting.framework.elasticsearch.builder.DefaultCompare
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-03-08 18:05
 */
@Component
class SpaceDataScope : ElasticsearchDataScope {

    override val resource: String = "null"

    override fun includes(index: String): Boolean {
        return true
    }

    override fun handler(p: IndexInfo): Query? {
        val compare = DefaultCompare<Any>()
            .term("type", "space")
        return compare.buildQuery()
    }

}
