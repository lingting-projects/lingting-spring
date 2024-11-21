package live.lingting.spring.grpc;

import live.lingting.spring.grpc.annotation.EnableGrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lingting 2024-03-26 10:10
 */
@EnableGrpcServer
@SpringBootApplication
public class SpringGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGrpcApplication.class, args);
	}

}
