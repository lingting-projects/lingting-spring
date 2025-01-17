package live.lingting.spring.elasticsearch

import java.util.concurrent.atomic.AtomicLong
import live.lingting.framework.util.ThreadUtils
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.elasticsearch.client.RestClientBuilder
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer

/**
 * @author lingting 2025/1/17 14:24
 */
open class DefaultRestClientBuilderCustomizer : RestClientBuilderCustomizer {

    override fun customize(builder: RestClientBuilder) {
        builder.setHttpClientConfigCallback {
            it.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build())

            val factory = ThreadUtils.executor().threadFactory
            val counter = AtomicLong()
            it.setThreadFactory {
                val thread = factory.newThread(it)
                thread.name = "es-${counter.incrementAndGet()}"
                thread
            }
        }

    }

}
