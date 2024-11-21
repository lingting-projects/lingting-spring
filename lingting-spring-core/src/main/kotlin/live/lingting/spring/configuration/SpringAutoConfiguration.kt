package live.lingting.spring.configuration;

import live.lingting.framework.context.ContextComponent;
import live.lingting.framework.sensitive.serializer.SensitiveAllSerializer;
import live.lingting.framework.sensitive.serializer.SensitiveDefaultSerializer;
import live.lingting.framework.sensitive.serializer.SensitiveMobileSerializer;
import live.lingting.spring.event.SpringContextClosedListener;
import live.lingting.spring.mdc.MdcTaskDecorator;
import live.lingting.spring.post.ContextComposeBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;

/**
 * @author lingting 2023-04-24 21:32
 */
@AutoConfiguration
public class SpringAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(ContextComponent.class)
	public ContextComposeBeanPostProcessor contextComposeBeanPostProcessor() {
		return new ContextComposeBeanPostProcessor();
	}

	@Bean
	@ConditionalOnMissingBean
	public TaskDecorator taskDecorator() {
		return new MdcTaskDecorator();
	}

	@Bean
	@ConditionalOnMissingBean
	public SpringContextClosedListener springContextClosedListener() {
		return new SpringContextClosedListener();
	}

	// region Sensitive

	@Bean
	@ConditionalOnMissingBean
	public SensitiveAllSerializer sensitiveAllSerializer() {
		return SensitiveAllSerializer.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public SensitiveDefaultSerializer sensitiveDefaultSerializer() {
		return SensitiveDefaultSerializer.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public SensitiveMobileSerializer sensitiveMobileSerializer() {
		return SensitiveMobileSerializer.INSTANCE;
	}

	// endregion

}
