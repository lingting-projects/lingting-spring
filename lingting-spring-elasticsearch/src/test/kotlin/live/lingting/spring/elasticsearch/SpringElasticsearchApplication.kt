package live.lingting.spring.elasticsearch;

import live.lingting.spring.jackson.configuration.SpringJacksonModuleAutoConfiguration;
import live.lingting.spring.jackson.configuration.SpringObjectMapperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-03-08 17:30
 */
@SpringBootApplication
@ImportAutoConfiguration({ SpringObjectMapperAutoConfiguration.class, SpringJacksonModuleAutoConfiguration.class })
public class SpringElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticsearchApplication.class, args);
	}

}
