package live.lingting.spring.elasticsearch

import live.lingting.framework.thread.platform.PlatformThread
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.elasticsearch.client.RestClientBuilder
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer

/**
 * @author lingting 2025/1/17 14:24
 */
open class DefaultRestClientBuilderCustomizer(private val properties: ElasticsearchSpringProperties) :
    RestClientBuilderCustomizer {

    override fun customize(builder: RestClientBuilder) {
        builder.setHttpClientConfigCallback {
            httpAsyncClientBuilder(it)
        }

    }

    protected open fun httpAsyncClientBuilder(builder: HttpAsyncClientBuilder): HttpAsyncClientBuilder? {
        val ioConfigBuilder = IOReactorConfig.custom().setSoKeepAlive(true)

        val factory = if (properties.useVirtualThread) {
            Thread.ofVirtual().name("es-vt-", 0).factory()
        } else {
            PlatformThread.threadFactory() ?: Thread.ofPlatform().name("es-t-", 0).factory()
        }

        builder.setDefaultIOReactorConfig(ioConfigBuilder.build())
        return builder.setThreadFactory(factory)
    }

}
