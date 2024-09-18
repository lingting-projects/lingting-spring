package live.lingting.spring.huawei;

import live.lingting.framework.huawei.HuaweiIam;
import live.lingting.framework.huawei.HuaweiObsBucket;
import live.lingting.framework.huawei.properties.HuaweiIamProperties;
import live.lingting.framework.huawei.properties.HuaweiObsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(HuaweiSpringProperties.class)
public class HuaweiAutoConfiguration {

	@Bean
	public HuaweiIamProperties huaweiIamProperties(HuaweiSpringProperties properties) {
		return properties.getIam();
	}

	@Bean
	public HuaweiObsProperties huaweiObsProperties(HuaweiSpringProperties properties) {
		return properties.getObs();
	}

	@Bean
	@ConditionalOnProperty(prefix = HuaweiSpringProperties.PREFIX + ".iam", name = "username")
	public HuaweiIam huaweiIam(HuaweiIamProperties properties) {
		return new HuaweiIam(properties);
	}

	@Bean
	@ConditionalOnProperty(prefix = HuaweiSpringProperties.PREFIX + ".obs", name = "ak")
	public HuaweiObsBucket huaweiObsBucket(HuaweiObsProperties properties) {
		return new HuaweiObsBucket(properties);
	}

}
