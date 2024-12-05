package live.lingting.spring.web.converter

import jakarta.annotation.Resource
import live.lingting.framework.util.Slf4jUtils.logger
import live.lingting.framework.value.WaitValue
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.ConverterRegistry

/**
 * @author lingting 2024-03-20 15:51
 */
abstract class AbstractConverter<T> : Converter<T> {
    val log = logger()

    private val serviceValue = WaitValue.of<ConversionService>()

    var service: ConversionService
        get() = serviceValue.notNull()
        @Resource
        set(value) {
            serviceValue.update(value)
        }

    private val registryValue = WaitValue.of<ConverterRegistry>()

    var registry: ConverterRegistry
        get() = registryValue.notNull()
        @Resource
        set(value) {
            value.addConverter(this)
            registryValue.update(value)
        }

    fun <E> convert(source: Any?, target: Class<E>): E? {
        if (source == null) {
            return null
        }
        if (!service.canConvert(source.javaClass, target)) {
            log.debug("无法将类型[{}]转为[{}]", source.javaClass, target)
            return null
        }
        return service.convert<E>(source, target)
    }

}
