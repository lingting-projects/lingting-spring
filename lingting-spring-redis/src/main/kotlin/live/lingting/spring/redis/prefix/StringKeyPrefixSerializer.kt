package live.lingting.spring.redis.prefix

import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * @author lingting 2024-04-17 16:05
 */
@Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
class StringKeyPrefixSerializer(private val convert: KeyPrefixConvert) : StringRedisSerializer() {
    override fun serialize(value: String): ByteArray {
        val bytes = super.serialize(value)
        return if (convert.isMatch(bytes)) convert.wrap(bytes) else bytes
    }

    override fun deserialize(bytes: ByteArray): String {
        val target = if (convert.isMatch(bytes)) convert.unwrap(bytes) else bytes
        return super.deserialize(target)
    }
}
