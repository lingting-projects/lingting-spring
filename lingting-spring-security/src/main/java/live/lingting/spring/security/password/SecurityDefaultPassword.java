package live.lingting.spring.security.password;

import live.lingting.framework.security.password.SecurityPassword;
import live.lingting.framework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lingting 2024-02-05 17:26
 */
@RequiredArgsConstructor
public class SecurityDefaultPassword implements SecurityPassword {

	private final String securityKey;

	private final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	@Override
	public boolean valid(String plaintext) {
		return StringUtils.hasText(plaintext);
	}

	private BytesEncryptor frontEncryptor() {
		String salt = StringUtils.hex(securityKey.getBytes(StandardCharsets.UTF_8));
		return Encryptors.standard(securityKey, salt);
	}

	/**
	 * 依据前端加密方式, 明文转密文
	 */
	@Override
	public String encodeFront(String plaintext) {
		BytesEncryptor encryptor = frontEncryptor();
		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

		byte[] bytes = encryptor.encrypt(plaintext.getBytes(StandardCharsets.UTF_8));
		return encoder.encodeToString(bytes);
	}

	/**
	 * 解析收到的前端密文
	 */
	@Override
	public String decodeFront(String ciphertext) {
		BytesEncryptor encryptor = frontEncryptor();
		java.util.Base64.Decoder decoder = Base64.getDecoder();

		byte[] bytes = decoder.decode(ciphertext.getBytes(StandardCharsets.UTF_8));
		byte[] result = encryptor.decrypt(bytes);
		return new String(result, StandardCharsets.UTF_8);
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
