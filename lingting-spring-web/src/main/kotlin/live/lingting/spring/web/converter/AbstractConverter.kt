package live.lingting.spring.web.converter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService

/**
 * @author lingting 2024-03-20 15:51
 */
abstract class AbstractConverter<T> : Converter<T> {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    var service: ConversionService? = null

    fun <E> convert(source: Any?, target: Class<E>): E? {
        if (source == null) {
            return null
        }
        if (!service!!.canConvert(source.javaClass, target)) {
            log.debug("无法将类型[{}]转为[{}]", source.javaClass, target)
            return null
        }
        return service!!.convert<E>(source, target)
    }

}
