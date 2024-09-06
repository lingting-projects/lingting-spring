package live.lingting.spring.security.password;

import live.lingting.framework.crypto.cipher.Cipher;
import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.util.StringUtils;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author lingting 2024-02-05 17:26
 */
public class SecurityDefaultPassword implements SecurityPassword {

	protected final String securityKey;

	protected final Cipher front;

	protected final BCryptPasswordEncoder encode;

	public SecurityDefaultPassword(String securityKey) {
		this.securityKey = securityKey;
		this.front = Cipher.aesBuilder().secret(securityKey).iv(securityKey).cbc().pkcs7().build();
		this.encode = new BCryptPasswordEncoder();
	}

	@Override
	public boolean valid(String plaintext) {
		return StringUtils.hasText(plaintext);
	}

	/**
	 * 依据前端加密方式, 明文转密文
	 */
	@SneakyThrows
	@Override
	public String encodeFront(String plaintext) {
		return front.encryptBase64(plaintext);
	}

	/**
	 * 解析收到的前端密文
	 */
	@SneakyThrows
	@Override
	public String decodeFront(String ciphertext) {
		return front.decryptBase64(ciphertext);
	}

	/**
	 * 密码明文转数据库存储的密文
	 */
	@Override
	public String encode(String plaintext) {
		return encode.encode(plaintext);
	}

	/**
	 * 明文和密文是否匹配
	 */
	@Override
	public boolean match(String plaintext, String ciphertext) {
		return encode.matches(plaintext, ciphertext);
	}

}
