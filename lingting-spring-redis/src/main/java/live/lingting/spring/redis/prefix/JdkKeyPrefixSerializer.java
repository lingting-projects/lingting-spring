package live.lingting.spring.redis.prefix;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * @author lingting 2024-04-17 16:10
 */
@RequiredArgsConstructor
public class JdkKeyPrefixSerializer extends JdkSerializationRedisSerializer {

	private final KeyPrefixConvert convert;

	@Override
	public byte[] serialize(Object value) {
		byte[] bytes = super.serialize(value);
		return convert.isMatch(bytes) ? convert.wrap(bytes) : bytes;
	}

	@Override
	public Object deserialize(byte[] bytes) {
		byte[] target = convert.isMatch(bytes) ? convert.unwrap(bytes) : bytes;
		return super.deserialize(target);
	}

}
