package live.lingting.spring.mybatis

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import live.lingting.framework.mybatis.methods.AbstractMybatisMethod
import live.lingting.framework.mybatis.methods.InsertIgnore
import live.lingting.framework.mybatis.methods.MethodsInjector
import live.lingting.framework.mybatis.typehandler.AutoRegisterTypeHandler
import live.lingting.spring.jackson.ObjectMapperAfter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * @author lingting 2024-03-11 10:49
 */
@AutoConfiguration
open class SpringMybatisAutoConfiguration {

    /**
     * MybatisPlusInterceptor 插件，默认提供分页插件
     * 如需其他MP内置插件，则需自定义该Bean
     * @return MybatisPlusInterceptor
     */
    @Bean
    @ConditionalOnMissingBean
    open fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    @Bean
    open fun mybatisObjectMapperAfter(): ObjectMapperAfter {
        return ObjectMapperAfter { objectMapper -> JacksonTypeHandler.setObjectMapper(objectMapper) }
    }

    @Bean
    @ConditionalOnMissingBean
    open fun extendServiceImplBeanPost(): ExtendServiceImplBeanPost {
        return ExtendServiceImplBeanPost()
    }

    @Bean
    open fun autoRegisterConfigurationCustomizer(list: MutableList<AutoRegisterTypeHandler<*>>): ConfigurationCustomizer {
        return ConfigurationCustomizer { configuration ->
            val registry = configuration.getTypeHandlerRegistry()
            for (handler in list) {
                registry.register(handler)
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    open fun frameworkIgnore(): InsertIgnore {
        return InsertIgnore()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun frameworkMethodsInjector(methods: List<AbstractMybatisMethod>): MethodsInjector {
        return MethodsInjector(methods)
    }

}
