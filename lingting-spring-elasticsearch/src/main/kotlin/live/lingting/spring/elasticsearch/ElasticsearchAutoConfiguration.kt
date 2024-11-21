package live.lingting.spring.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.lingting.framework.elasticsearch.ElasticsearchProperties;
import live.lingting.framework.elasticsearch.datascope.DefaultElasticsearchDataPermissionHandler;
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataPermissionHandler;
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataScope;
import live.lingting.spring.jackson.configuration.SpringObjectMapperAutoConfiguration;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2024-03-08 15:37
 */
@EnableConfigurationProperties(ElasticsearchSpringProperties.class)
@AutoConfiguration(
		after = { ElasticsearchRestClientAutoConfiguration.class, SpringObjectMapperAutoConfiguration.class })
public class ElasticsearchAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ElasticsearchProperties elasticsearchProperties(ElasticsearchSpringProperties properties) {
		return properties.properties();
	}

	@Bean
	public RestClientBuilderCustomizer restClientBuilderCustomizer() {
		return builder -> builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
			.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build()));
	}

	@Bean
	@ConditionalOnBean(ObjectMapper.class)
	@ConditionalOnMissingBean(JsonpMapper.class)
	public JsonpMapper jsonpMapper(ObjectMapper mapper) {
		return new JacksonJsonpMapper(mapper);
	}

	@Bean
	@ConditionalOnBean(RestClientBuilder.class)
	@ConditionalOnMissingBean(RestClient.class)
	public RestClient restClient(RestClientBuilder builder) {
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({ RestClient.class, JsonpMapper.class })
	public ElasticsearchTransport restClientTransport(RestClient restClient, JsonpMapper jsonpMapper) {
		return new RestClientTransport(restClient, jsonpMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ElasticsearchTransport.class)
	public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
		return new ElasticsearchClient(transport);
	}

	@Bean
	@ConditionalOnMissingBean
	public ElasticsearchDataPermissionHandler elasticsearchDataPermissionHandler(List<ElasticsearchDataScope> scopes) {
		return new DefaultElasticsearchDataPermissionHandler(scopes);
	}

	@Bean
	@ConditionalOnMissingBean
	public ElasticsearchServiceImplBeanPost elasticsearchServiceImplBeanPost(ElasticsearchProperties properties,
			ElasticsearchClient client, ElasticsearchDataPermissionHandler handler) {
		return new ElasticsearchServiceImplBeanPost(properties, client, handler);
	}

}
