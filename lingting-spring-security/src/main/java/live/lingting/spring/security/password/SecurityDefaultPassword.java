package live.lingting.spring.security.password;

import live.lingting.framework.cipher.AES;
import live.lingting.framework.cipher.AbstractCrypt;
import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author lingting 2024-02-05 17:26
 */
@RequiredArgsConstructor
public class SecurityDefaultPassword implements SecurityPassword {

	private final String securityKey;

	private final AES front = AES.builder().cbc().pkcs7().build();

	private final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	@Override
	public boolean valid(String plaintext) {
		return StringUtils.hasText(plaintext);
	}

	<T extends AbstractCrypt<T>> T fill(T t) {
		return t.secret(securityKey).iv(securityKey);
	}

	/**
	 * 依据前端加密方式, 明文转密文
	 */
	@SneakyThrows
	@Override
	public String encodeFront(String plaintext) {
		return fill(front.encrypt()).base64(plaintext);
	}

	/**
	 * 解析收到的前端密文
	 */
	@SneakyThrows
	@Override
	public String decodeFront(String ciphertext) {
		return fill(front.decrypt()).base64(ciphertext);
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
