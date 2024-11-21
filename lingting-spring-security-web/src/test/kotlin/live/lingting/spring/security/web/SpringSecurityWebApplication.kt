package live.lingting.spring.security.web;

import live.lingting.spring.security.annotation.EnableAuthorizationServer;
import live.lingting.spring.security.annotation.EnableResourceServer;
import live.lingting.spring.security.configuration.SecurityAutoConfiguration;
import live.lingting.spring.web.annotation.EnableWebExceptionHandler;
import live.lingting.spring.web.configuration.SpringWebAutoConfiguration;
import live.lingting.spring.web.configuration.SpringWebConverterAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-03-21 20:16
 */
@EnableResourceServer
@SpringBootApplication
@EnableWebExceptionHandler
@EnableAuthorizationServer
@ImportAutoConfiguration({ SpringWebAutoConfiguration.class, SpringWebConverterAutoConfiguration.class,
		SecurityAutoConfiguration.class })
public class SpringSecurityWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityWebApplication.class, args);
	}

}
