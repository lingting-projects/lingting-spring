package live.lingting.spring.post

import live.lingting.spring.util.EnvironmentUtils
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.ConfigurableEnvironment

/**
 * 高优先级, 用于注入环境配置到工具类
 *
 * @author lingting 2022/10/15 11:33
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpringEnvironmentPostProcessor : EnvironmentPostProcessor {

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        EnvironmentUtils.environment = environment
    }

}
