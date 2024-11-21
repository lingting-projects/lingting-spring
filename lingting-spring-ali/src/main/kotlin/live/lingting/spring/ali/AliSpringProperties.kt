package live.lingting.spring.ali;

import live.lingting.framework.ali.properties.AliOssProperties;
import live.lingting.framework.ali.properties.AliStsProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(AliSpringProperties.PREFIX)
public class AliSpringProperties {

	public static final String PREFIX = "lingting.ali";

	@NestedConfigurationProperty
	private AliStsProperties sts = new AliStsProperties();

	@NestedConfigurationProperty
	private AliOssProperties oss = new AliOssProperties();

	public AliStsProperties getSts() {
		return this.sts;
	}

	public AliOssProperties getOss() {
		return this.oss;
	}

	public void setSts(AliStsProperties sts) {
		this.sts = sts;
	}

	public void setOss(AliOssProperties oss) {
		this.oss = oss;
	}

}
