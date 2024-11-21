package live.lingting.spring.configuration

import live.lingting.framework.context.ContextComponent
import live.lingting.framework.sensitive.serializer.SensitiveAllSerializer
import live.lingting.framework.sensitive.serializer.SensitiveDefaultSerializer
import live.lingting.framework.sensitive.serializer.SensitiveMobileSerializer
import live.lingting.spring.event.SpringContextClosedListener
import live.lingting.spring.mdc.MdcTaskDecorator
import live.lingting.spring.post.ContextComposeBeanPostProcessor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.core.task.TaskDecorator

/**
 * @author lingting 2023-04-24 21:32
 */
@AutoConfiguration
class SpringAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ContextComponent::class)
    fun contextComposeBeanPostProcessor(): ContextComposeBeanPostProcessor {
        return ContextComposeBeanPostProcessor()
    }

    @Bean
    @ConditionalOnMissingBean
    fun taskDecorator(): TaskDecorator {
        return MdcTaskDecorator()
    }

    @Bean
    @ConditionalOnMissingBean
    fun springContextClosedListener(): SpringContextClosedListener {
        return SpringContextClosedListener()
    }

    // region Sensitive
    @Bean
    @ConditionalOnMissingBean
    fun sensitiveAllSerializer(): SensitiveAllSerializer {
        return SensitiveAllSerializer
    }

    @Bean
    @ConditionalOnMissingBean
    fun sensitiveDefaultSerializer(): SensitiveDefaultSerializer {
        return SensitiveDefaultSerializer
    }

    @Bean
    @ConditionalOnMissingBean
    fun sensitiveMobileSerializer(): SensitiveMobileSerializer {
        return SensitiveMobileSerializer
    } // endregion
}
