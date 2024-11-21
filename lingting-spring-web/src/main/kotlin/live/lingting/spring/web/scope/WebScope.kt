package live.lingting.spring.web.scope

/**
 * @author lingting 2024-03-20 15:02
 */
class WebScope(
    val scheme: String, val host: String, val origin: String, val ip: String, val uri: String, @JvmField val traceId: String, @JvmField val requestId: String,
    val language: String, val authorization: String, val userAgent: String
) {
    val attributes: MutableMap<String, Any> = HashMap<String, Any>()

    fun putAttribute(key: String, value: Any) {
        this.attributes.put(key, value)
    }

    fun <T> getAttribute(key: String): T {
        return this.attributes.get(key) as T
    }
}
