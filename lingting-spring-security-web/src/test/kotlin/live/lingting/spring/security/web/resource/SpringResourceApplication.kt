package live.lingting.spring.security.web.resource

import live.lingting.spring.security.annotation.EnableResourceServer
import live.lingting.spring.security.configuration.SecurityAutoConfiguration
import live.lingting.spring.web.annotation.EnableWebExceptionHandler
import live.lingting.spring.web.configuration.SpringWebAutoConfiguration
import live.lingting.spring.web.configuration.SpringWebConverterAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-09-07 16:09
 */
@EnableResourceServer
@SpringBootApplication
@EnableWebExceptionHandler
@ImportAutoConfiguration(
    SpringWebAutoConfiguration::class, SpringWebConverterAutoConfiguration::class, SecurityAutoConfiguration::class
)
open class SpringResourceApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(SpringResourceApplication::class.java, *args)
    }
}
