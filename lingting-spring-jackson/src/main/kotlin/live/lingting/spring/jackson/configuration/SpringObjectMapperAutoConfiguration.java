package live.lingting.spring.jackson.configuration;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import live.lingting.framework.jackson.JacksonUtils;
import live.lingting.spring.jackson.ObjectMapperAfter;
import live.lingting.spring.jackson.ObjectMapperCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * @author lingting 2024-02-02 13:51
 */
@ConditionalOnClass(ObjectMapper.class)
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class SpringObjectMapperAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper(List<DefaultSerializerProvider> serializerProviders, List<Module> modules,
			List<ObjectMapperCustomizer> customizers, List<ObjectMapperAfter> afters) {
		customizers.sort(AnnotationAwareOrderComparator.INSTANCE);
		afters.sort(AnnotationAwareOrderComparator.INSTANCE);

		ObjectMapper mapper = new ObjectMapper();

		// 序列化时忽略未知属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 单值元素可以被设置成 array, 防止处理 ["a"] 为 List<String> 时报错
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		// 空对象不报错
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 有特殊需要转义字符, 不报错
		mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());

		for (DefaultSerializerProvider serializerProvider : serializerProviders) {
			mapper.setSerializerProvider(serializerProvider);
		}

		for (Module module : modules) {
			mapper.registerModule(module);
		}

		for (ObjectMapperCustomizer mapperCustomizer : customizers) {
			mapper = mapperCustomizer.apply(mapper);
		}

		JacksonUtils.config(mapper);
		for (ObjectMapperAfter after : afters) {
			after.apply(mapper);
		}
		return mapper;
	}

}
