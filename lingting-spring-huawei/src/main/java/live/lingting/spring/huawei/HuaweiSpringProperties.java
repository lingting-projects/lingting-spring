package live.lingting.spring.huawei;

import live.lingting.framework.huawei.properties.HuaweiIamProperties;
import live.lingting.framework.huawei.properties.HuaweiObsProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-09-18 20:47
 */
@ConfigurationProperties(HuaweiSpringProperties.PREFIX)
public class HuaweiSpringProperties {

	public static final String PREFIX = "lingting.huawei";

	@NestedConfigurationProperty
	private HuaweiIamProperties iam = new HuaweiIamProperties();

	@NestedConfigurationProperty
	private HuaweiObsProperties obs = new HuaweiObsProperties();

	public HuaweiIamProperties getIam() {
		return this.iam;
	}

	public HuaweiObsProperties getObs() {
		return this.obs;
	}

	public void setIam(HuaweiIamProperties iam) {
		this.iam = iam;
	}

	public void setObs(HuaweiObsProperties obs) {
		this.obs = obs;
	}

}
