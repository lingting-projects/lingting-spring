package live.lingting.spring.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-09-18 20:57
 */
@SpringBootApplication
@ImportAutoConfiguration({ AwsAutoConfiguration.class })
public class SpringAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAwsApplication.class, args);
	}

}
