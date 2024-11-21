package live.lingting.spring.security.password

import live.lingting.framework.crypto.AbstractCryptoBuilder.build
import live.lingting.framework.crypto.AbstractCryptoBuilder.iv
import live.lingting.framework.crypto.AbstractCryptoBuilder.secret
import live.lingting.framework.crypto.cipher.Cipher
import live.lingting.framework.crypto.cipher.CipherBuilder.AES.cbc
import live.lingting.framework.crypto.cipher.CipherBuilder.AES.pkcs7
import live.lingting.framework.security.password.SecurityPassword
import live.lingting.framework.util.StringUtils.hasText
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * @author lingting 2024-02-05 17:26
 */
class SecurityDefaultPassword(securityKey: String) : SecurityPassword {
    protected val securityKey: String

    protected val front: Cipher

    protected val encode: BCryptPasswordEncoder

    init {
        this.securityKey = securityKey
        this.front = aesBuilder.aesBuilder().secret(securityKey).iv(securityKey).cbc().pkcs7().build()
        this.encode = BCryptPasswordEncoder()
    }

    override fun valid(plaintext: String): Boolean {
        return hasText(plaintext)
    }

    /**
     * 依据前端加密方式, 明文转密文
     */
    override fun encodeFront(plaintext: String): String {
        return front.encryptBase64(plaintext)
    }

    /**
     * 解析收到的前端密文
     */
    override fun decodeFront(ciphertext: String): String {
        return front.decryptBase64(ciphertext)
    }

    /**
     * 密码明文转数据库存储的密文
     */
    override fun encode(plaintext: String): String {
        return encode.encode(plaintext)
    }

    /**
     * 明文和密文是否匹配
     */
    override fun match(plaintext: String, ciphertext: String): Boolean {
        return encode.matches(plaintext, ciphertext)
    }
}
