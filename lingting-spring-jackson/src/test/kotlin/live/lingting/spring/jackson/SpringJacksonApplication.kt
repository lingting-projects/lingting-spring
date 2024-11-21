package live.lingting.spring.jackson

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-02-02 15:56
 */
@SpringBootApplication
object SpringJacksonApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(SpringJacksonApplication::class.java, *args)
    }
}
