package live.lingting.spring.redis.prefix;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lingting 2024-04-17 16:05
 */
@RequiredArgsConstructor
public class StringKeyPrefixSerializer extends StringRedisSerializer {

	private final KeyPrefixConvert convert;

	@Override
	public byte[] serialize(String value) {
		byte[] bytes = super.serialize(value);
		return convert.isMatch(bytes) ? convert.wrap(bytes) : bytes;
	}

	@Override
	public String deserialize(byte[] bytes) {
		byte[] target = convert.isMatch(bytes) ? convert.unwrap(bytes) : bytes;
		return super.deserialize(target);
	}

}
