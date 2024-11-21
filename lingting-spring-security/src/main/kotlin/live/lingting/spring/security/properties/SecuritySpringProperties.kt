package live.lingting.spring.security.properties

import live.lingting.framework.security.properties.SecurityProperties
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2023-03-29 20:50
 */
@ConfigurationProperties(prefix = SecuritySpringProperties.PREFIX)
class SecuritySpringProperties {
    var authorization: Authorization = Authorization()

    /**
     * 鉴权优先级. 降序排序
     */
    var order: Int = -500

    fun properties(): SecurityProperties {
        val sa = SecurityProperties.Authorization()
        val a = this.authorization
        if (a != null) {
            sa.isRemote = a.isRemote
            sa.remoteHost = a.remoteHost
        }

        val properties = SecurityProperties()
        properties.authorization = sa
        properties.order = this.order
        return properties
    }

    class Authorization {
        var isRemote: Boolean = false

        var remoteHost: String = null

        /**
         * 前后端交互使用的对称加密算法的密钥，必须 16 位字符
         */
        var passwordSecretKey: String = null
    }

    companion object {
        const val PREFIX: String = "lingting.security"
    }
}
