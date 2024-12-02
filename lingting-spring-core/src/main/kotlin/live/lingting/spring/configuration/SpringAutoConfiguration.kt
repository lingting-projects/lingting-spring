package live.lingting.spring.configuration

import live.lingting.framework.application.ApplicationComponent
import live.lingting.framework.sensitive.serializer.SensitiveAllSerializer
import live.lingting.framework.sensitive.serializer.SensitiveDefaultSerializer
import live.lingting.framework.sensitive.serializer.SensitiveMobileSerializer
import live.lingting.spring.event.SpringContextClosedListener
import live.lingting.spring.mdc.MdcTaskDecorator
import live.lingting.spring.post.ApplicationComponentBeanPostProcessor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.core.task.TaskDecorator

/**
 * @author lingting 2023-04-24 21:32
 */
@AutoConfiguration
open class SpringAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ApplicationComponent::class)
    open fun contextComposeBeanPostProcessor(): ApplicationComponentBeanPostProcessor {
        return ApplicationComponentBeanPostProcessor()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun taskDecorator(): TaskDecorator {
        return MdcTaskDecorator()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun springContextClosedListener(): SpringContextClosedListener {
        return SpringContextClosedListener()
    }

    // region Sensitive
    @Bean
    @ConditionalOnMissingBean
    open fun sensitiveAllSerializer(): SensitiveAllSerializer {
        return SensitiveAllSerializer
    }

    @Bean
    @ConditionalOnMissingBean
    open fun sensitiveDefaultSerializer(): SensitiveDefaultSerializer {
        return SensitiveDefaultSerializer
    }

    @Bean
    @ConditionalOnMissingBean
    open fun sensitiveMobileSerializer(): SensitiveMobileSerializer {
        return SensitiveMobileSerializer
    }
// endregion
}
