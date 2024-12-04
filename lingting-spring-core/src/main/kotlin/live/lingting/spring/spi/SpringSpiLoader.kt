package live.lingting.spring.spi

import live.lingting.spring.util.SpringUtils

/**
 * @author lingting 2024/12/4 11:56
 */
interface SpringSpiLoader<S> {

    val cls: Class<S>

    fun all(): Collection<S> {
        if (SpringUtils.context == null) {
            return emptyList()
        }
        return SpringUtils.getBeansOfType(cls).values
    }

}
