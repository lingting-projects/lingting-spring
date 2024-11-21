package live.lingting.spring.elasticsearch

import org.springframework.stereotype.Component

/**
 * @author lingting 2024-03-08 18:08
 */
@Component
class EntityServiceImpl : AbstractElasticsearchServiceImpl<SpringElasticsearchTest.Entity>() {
    override fun documentId(entity: SpringElasticsearchTest.Entity): String {
        return entity.id
    }
}
