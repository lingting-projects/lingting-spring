package live.lingting.spring.elasticsearch

import live.lingting.framework.elasticsearch.polymerize.Polymerize
import live.lingting.framework.elasticsearch.polymerize.PolymerizeFactory
import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024/11/26 15:12
 */
class SpringPolymerizeFactory : PolymerizeFactory() {

    override fun create(clazz: Class<out Polymerize>): Polymerize {
        return SpringUtils.ofBean(clazz)
    }
}
