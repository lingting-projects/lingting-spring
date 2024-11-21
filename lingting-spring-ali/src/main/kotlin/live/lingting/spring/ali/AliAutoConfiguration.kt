package live.lingting.spring.ali;

import live.lingting.framework.ali.AliOssBucket;
import live.lingting.framework.ali.AliSts;
import live.lingting.framework.ali.properties.AliOssProperties;
import live.lingting.framework.ali.properties.AliStsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2024-09-18 20:45
 */
@AutoConfiguration
@EnableConfigurationProperties(AliSpringProperties.class)
public class AliAutoConfiguration {

	@Bean
	public AliStsProperties aliStsProperties(AliSpringProperties properties) {
		return properties.getSts();
	}

	@Bean
	public AliOssProperties aliOssProperties(AliSpringProperties properties) {
		return properties.getOss();
	}

	@Bean
	@ConditionalOnProperty(prefix = AliSpringProperties.PREFIX + ".sts", name = "ak")
	public AliSts aliSts(AliStsProperties properties) {
		return new AliSts(properties);
	}

	@Bean
	@ConditionalOnProperty(prefix = AliSpringProperties.PREFIX + ".oss", name = "ak")
	public AliOssBucket aliOssBucket(AliOssProperties properties) {
		return new AliOssBucket(properties);
	}

}
