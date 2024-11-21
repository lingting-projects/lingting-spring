package live.lingting.spring.redis.prefix

/**
 * @author lingting 2024-04-17 15:14
 */
interface KeyPrefixConvert {
    fun isMatch(key: ByteArray): Boolean

    /**
     * 包装key
     */
    fun wrap(key: ByteArray): ByteArray

    /**
     * 解包key
     */
    fun unwrap(key: ByteArray): ByteArray
}
