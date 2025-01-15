package live.lingting.spring.elasticsearch.configuration

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.JsonpMapper
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.atomic.AtomicLong
import live.lingting.framework.Sequence
import live.lingting.framework.elasticsearch.ElasticsearchProperties
import live.lingting.framework.elasticsearch.interceptor.Interceptor
import live.lingting.framework.elasticsearch.polymerize.PolymerizeFactory
import live.lingting.framework.util.ThreadUtils
import live.lingting.spring.elasticsearch.ElasticsearchServiceImplBeanPost
import live.lingting.spring.elasticsearch.ElasticsearchSpringProperties
import live.lingting.spring.elasticsearch.SpringPolymerizeFactory
import live.lingting.spring.jackson.configuration.SpringObjectMapperAutoConfiguration
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-03-08 15:37
 */
@EnableConfigurationProperties(ElasticsearchSpringProperties::class)
@AutoConfiguration(after = [ElasticsearchRestClientAutoConfiguration::class, SpringObjectMapperAutoConfiguration::class])
open class SpringElasticsearchAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    open fun elasticsearchProperties(properties: ElasticsearchSpringProperties): ElasticsearchProperties {
        return properties.properties()
    }

    @Bean
    open fun restClientBuilderCustomizer(): RestClientBuilderCustomizer {
        return RestClientBuilderCustomizer { builder ->
            builder!!.setHttpClientConfigCallback(HttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder!!
                    .setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build())
            })
        }
    }

    @Bean
    @ConditionalOnBean(ObjectMapper::class)
    @ConditionalOnMissingBean(JsonpMapper::class)
    open fun jsonpMapper(mapper: ObjectMapper): JsonpMapper {
        return JacksonJsonpMapper(mapper)
    }

    @Bean
    @ConditionalOnBean(RestClientBuilder::class)
    @ConditionalOnMissingBean(RestClient::class)
    open fun restClient(builder: RestClientBuilder): RestClient {
        builder.setHttpClientConfigCallback {
            val factory = ThreadUtils.executor().threadFactory
            val counter = AtomicLong()
            it.setThreadFactory {
                val thread = factory.newThread(it)
                thread.name = "es-${counter.incrementAndGet()}"
                thread
            }
        }
        return builder.build()
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RestClient::class, JsonpMapper::class)
    open fun restClientTransport(restClient: RestClient, jsonpMapper: JsonpMapper): ElasticsearchTransport {
        return RestClientTransport(restClient, jsonpMapper)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ElasticsearchTransport::class)
    open fun elasticsearchClient(transport: ElasticsearchTransport): ElasticsearchClient {
        return ElasticsearchClient(transport)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun polymerizeFactory(): PolymerizeFactory {
        return SpringPolymerizeFactory()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun elasticsearchServiceImplBeanPost(
        properties: ElasticsearchProperties,
        client: ElasticsearchClient,
        interceptors: Collection<Interceptor>,
        polymerizeFactory: PolymerizeFactory,
    ): ElasticsearchServiceImplBeanPost {
        val asc = Sequence.asc(interceptors)
        return ElasticsearchServiceImplBeanPost(properties, client, asc, polymerizeFactory)
    }

}
