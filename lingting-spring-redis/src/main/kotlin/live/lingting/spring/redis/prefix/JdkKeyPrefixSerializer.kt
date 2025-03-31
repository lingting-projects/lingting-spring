package live.lingting.spring.redis.prefix

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer

/**
 * @author lingting 2024-04-17 16:10
 */
@Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
class JdkKeyPrefixSerializer(private val convert: KeyPrefixConvert) : JdkSerializationRedisSerializer() {

    override fun serialize(value: Any?): ByteArray {
        val bytes = super.serialize(value)
        return if (convert.isMatch(bytes)) convert.wrap(bytes) else bytes
    }

    override fun deserialize(bytes: ByteArray?): Any {
        val target = if (bytes != null && convert.isMatch(bytes)) convert.unwrap(bytes) else bytes
        return super.deserialize(target)
    }

}
