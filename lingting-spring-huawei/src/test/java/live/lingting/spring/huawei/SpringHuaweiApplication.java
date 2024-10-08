package live.lingting.spring.huawei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-09-18 20:57
 */
@SpringBootApplication
@ImportAutoConfiguration({ HuaweiAutoConfiguration.class })
public class SpringHuaweiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringHuaweiApplication.class, args);
	}

}
