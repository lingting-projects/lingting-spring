package live.lingting.spring.redis.prefix;

/**
 * @author lingting 2024-04-17 15:14
 */
public interface KeyPrefixConvert {

	boolean isMatch(byte[] key);

	/**
	 * 包装key
	 */
	byte[] wrap(byte[] key);

	/**
	 * 解包key
	 */
	byte[] unwrap(byte[] key);

}
