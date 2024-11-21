package live.lingting.spring.actuator

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.beans.factory.InitializingBean

/**
 * @author lingting 2023-07-25 18:30
 */
class MeterRegisterHandler(private val registry: MeterRegistry, private val binders: MutableList<MeterBinder>) : InitializingBean {
    fun registryAll() {
        for (binder in binders) {
            binder.bindTo(registry)
        }
    }


    override fun afterPropertiesSet() {
        registryAll()
    }
}
