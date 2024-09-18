package live.lingting.spring.huawei;

import live.lingting.framework.huawei.properties.HuaweiIamProperties;
import live.lingting.framework.huawei.properties.HuaweiObsProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2024-09-18 20:47
 */
@Getter
@Setter
@ConfigurationProperties(HuaweiSpringProperties.PREFIX)
public class HuaweiSpringProperties {

	public static final String PREFIX = "lingting.huawei";

	@NestedConfigurationProperty
	private HuaweiIamProperties iam = new HuaweiIamProperties();

	@NestedConfigurationProperty
	private HuaweiObsProperties obs = new HuaweiObsProperties();

}
