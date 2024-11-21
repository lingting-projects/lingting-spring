package live.lingting.spring.aws;

import live.lingting.framework.aws.s3.AwsS3Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(AwsSpringProperties.PREFIX)
public class AwsSpringProperties {

	public static final String PREFIX = "lingting.aws";

	@NestedConfigurationProperty
	private AwsS3Properties s3 = new AwsS3Properties();

	public AwsS3Properties getS3() {
		return this.s3;
	}

	public void setS3(AwsS3Properties s3) {
		this.s3 = s3;
	}

}
