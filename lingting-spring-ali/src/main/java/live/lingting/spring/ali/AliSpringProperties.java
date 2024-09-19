package live.lingting.spring.ali;

import live.lingting.framework.ali.properties.AliOssProperties;
import live.lingting.framework.ali.properties.AliStsProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-09-18 20:47
 */
@Getter
@Setter
@ConfigurationProperties(AliSpringProperties.PREFIX)
public class AliSpringProperties {

	public static final String PREFIX = "lingting.ali";

	@NestedConfigurationProperty
	private AliStsProperties sts = new AliStsProperties();

	@NestedConfigurationProperty
	private AliOssProperties oss = new AliOssProperties();

}
