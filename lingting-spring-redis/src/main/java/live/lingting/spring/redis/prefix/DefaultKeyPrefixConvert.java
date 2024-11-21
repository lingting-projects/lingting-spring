package live.lingting.spring.redis.prefix;

import live.lingting.framework.util.ArrayUtils;
import live.lingting.spring.redis.properties.RedisProperties;

import java.nio.charset.StandardCharsets;

/**
 * @author lingting 2024-04-17 15:12
 */
public class DefaultKeyPrefixConvert implements KeyPrefixConvert {

	private final String prefix;

	private final byte[] bytes;

	public DefaultKeyPrefixConvert(RedisProperties properties) {
		this.prefix = properties.getKeyPrefix();
		this.bytes = prefix.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public boolean isMatch(byte[] key) {
		return !ArrayUtils.isEmpty(bytes);
	}

	@Override
	public byte[] wrap(byte[] key) {
		if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(bytes)) {
			return key;
		}

		byte[] target = new byte[key.length + bytes.length];
		System.arraycopy(bytes, 0, target, 0, bytes.length);
		System.arraycopy(key, 0, target, bytes.length, key.length);
		return target;
	}

	@Override
	public byte[] unwrap(byte[] key) {
		if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(bytes)) {
			return key;
		}

		if (key.length < bytes.length) {
			return key;
		}

		for (int i = 0; i < bytes.length; i++) {
			// 不以前缀开头, 直接返回
			if (key[i] != bytes[i]) {
				return key;
			}
		}

		byte[] target = new byte[key.length - bytes.length];
		System.arraycopy(key, bytes.length, target, 0, target.length);
		return target;

	}

	public String getPrefix() {
		return this.prefix;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

}
