package live.lingting.spring.huawei

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-09-18 20:57
 */
@SpringBootApplication
@ImportAutoConfiguration(HuaweiAutoConfiguration::class)
object SpringHuaweiApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(SpringHuaweiApplication::class.java, *args)
    }
}
