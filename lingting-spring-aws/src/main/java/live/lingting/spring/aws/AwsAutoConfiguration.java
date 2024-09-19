package live.lingting.spring.aws;

import live.lingting.framework.aws.AwsS3Bucket;
import live.lingting.framework.aws.s3.AwsS3Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(AwsSpringProperties.class)
public class AwsAutoConfiguration {

	@Bean
	public AwsS3Properties awsS3Properties(AwsSpringProperties properties) {
		return properties.getS3();
	}

	@Bean
	@ConditionalOnProperty(prefix = AwsSpringProperties.PREFIX + ".s3", name = "ak")
	public AwsS3Bucket awsS3Bucket(AwsS3Properties properties) {
		return new AwsS3Bucket(properties);
	}

}
