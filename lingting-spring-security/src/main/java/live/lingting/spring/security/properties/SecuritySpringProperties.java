package live.lingting.spring.security.properties;

import live.lingting.framework.security.properties.SecurityProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-03-29 20:50
 */
@Data
@ConfigurationProperties(prefix = SecuritySpringProperties.PREFIX)
public class SecuritySpringProperties {

	public static final String PREFIX = "lingting.security";

	private Authorization authorization;

	@Data
	public static class Authorization {

		private boolean remote = false;

		private String remoteHost;

		/**
		 * 前后端交互使用的对称加密算法的密钥，必须 16 位字符
		 */
		private String passwordSecretKey;

	}

	public SecurityProperties properties() {
		SecurityProperties.Authorization sa = new SecurityProperties.Authorization();
		Authorization a = getAuthorization();
		if (a != null) {
			sa.setRemote(a.isRemote());
			sa.setRemoteHost(a.getRemoteHost());
		}

		SecurityProperties properties = new SecurityProperties();
		properties.setAuthorization(sa);
		return properties;
	}

}
