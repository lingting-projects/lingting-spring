package live.lingting.spring.elasticsearch

import co.elastic.clients.elasticsearch._types.query_dsl.Query
import live.lingting.framework.elasticsearch.composer.QueryComposer.term
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-03-08 18:05
 */
@Component
class SpaceDataScope : ElasticsearchDataScope {
    val resource: String
        get() = "null"

    override fun includes(index: String): Boolean {
        return true
    }

    override fun invoke(index: String): Query {
        return term<String>("space.name", "default")
    }
}
