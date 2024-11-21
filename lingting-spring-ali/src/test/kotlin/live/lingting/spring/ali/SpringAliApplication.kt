package live.lingting.spring.ali

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-09-18 20:57
 */
@SpringBootApplication
@ImportAutoConfiguration(AliAutoConfiguration::class)
object SpringAliApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(SpringAliApplication::class.java, *args)
    }
}
