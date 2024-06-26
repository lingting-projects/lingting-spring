package live.lingting.spring.web;

import live.lingting.spring.jackson.configuration.SpringJacksonModuleAutoConfiguration;
import live.lingting.spring.jackson.configuration.SpringObjectMapperAutoConfiguration;
import live.lingting.spring.web.annotation.EnableWebExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-03-20 17:28
 */
@SpringBootApplication
@EnableWebExceptionHandler
@ImportAutoConfiguration({ SpringJacksonModuleAutoConfiguration.class, SpringObjectMapperAutoConfiguration.class })
public class SpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}

}
