package live.lingting.spring.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import live.lingting.framework.elasticsearch.ElasticsearchApi;
import live.lingting.framework.elasticsearch.ElasticsearchProperties;
import live.lingting.framework.elasticsearch.ElasticsearchProvider;
import live.lingting.framework.elasticsearch.datascope.ElasticsearchDataPermissionHandler;
import live.lingting.spring.post.SpringBeanPostProcessor;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2024-03-08 16:52
 */
@RequiredArgsConstructor
@SuppressWarnings("java:S3740")
public class ElasticsearchServiceImplBeanPost implements SpringBeanPostProcessor {

	private final ElasticsearchProperties properties;

	private final ElasticsearchClient client;

	private final ElasticsearchDataPermissionHandler handler;

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean instanceof AbstractElasticsearchServiceImpl<?>;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Object postProcessAfter(Object bean, String beanName) {
		if (bean instanceof AbstractElasticsearchServiceImpl impl) {
			ElasticsearchApi api = ElasticsearchProvider.api(impl.index, impl.cls, impl::documentId, properties,
					handler, client);
			impl.setApi(api);
		}
		return bean;
	}

}
