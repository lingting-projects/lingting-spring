package live.lingting.spring.redis.prefix

import live.lingting.framework.util.ArrayUtils.isEmpty
import live.lingting.spring.redis.properties.RedisProperties
import java.nio.charset.StandardCharsets

/**
 * @author lingting 2024-04-17 15:12
 */
class DefaultKeyPrefixConvert(properties: RedisProperties) : KeyPrefixConvert {
    val prefix: String = properties.keyPrefix

    val bytes: ByteArray = prefix.toByteArray(StandardCharsets.UTF_8)

    override fun isMatch(key: ByteArray): Boolean {
        return !bytes.isEmpty()
    }

    override fun wrap(key: ByteArray): ByteArray {
        if (key.isEmpty() || bytes.isEmpty()) {
            return key
        }

        val target = ByteArray(key.size + bytes.size)
        System.arraycopy(bytes, 0, target, 0, bytes.size)
        System.arraycopy(key, 0, target, bytes.size, key.size)
        return target
    }

    override fun unwrap(key: ByteArray): ByteArray {
        if (key.isEmpty() || bytes.isEmpty()) {
            return key
        }

        if (key.size < bytes.size) {
            return key
        }

        for (i in bytes.indices) {
            // 不以前缀开头, 直接返回
            if (key[i] != bytes[i]) {
                return key
            }
        }

        val target = ByteArray(key.size - bytes.size)
        System.arraycopy(key, bytes.size, target, 0, target.size)
        return target
    }
}
