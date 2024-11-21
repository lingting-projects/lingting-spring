package live.lingting.spring.mybatis

import live.lingting.spring.jackson.configuration.SpringJacksonModuleAutoConfiguration
import live.lingting.spring.jackson.configuration.SpringObjectMapperAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-03-12 14:25
 */
@SpringBootApplication
@ImportAutoConfiguration(SpringJacksonModuleAutoConfiguration::class, SpringObjectMapperAutoConfiguration::class)
open class SpringMybatisApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(SpringMybatisApplication::class.java, *args)
    }
}
