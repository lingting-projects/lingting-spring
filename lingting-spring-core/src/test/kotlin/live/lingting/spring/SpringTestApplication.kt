package live.lingting.spring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author lingting 2024-02-02 14:48
 */
@SpringBootApplication
open class SpringTestApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(SpringTestApplication::class.java, *args)
    }
}
