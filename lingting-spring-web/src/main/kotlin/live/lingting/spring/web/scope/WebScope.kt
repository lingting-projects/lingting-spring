package live.lingting.spring.web.scope

/**
 * @author lingting 2024-03-20 15:02
 */
@Suppress("UNCHECKED_CAST")
class WebScope {

    @JvmOverloads
    constructor(
        scheme: String = "",
        host: String = "",
        origin: String = "",
        ip: String = "",
        uri: String = "",
        traceId: String = "",
        requestId: String = "",
        language: String = "",
        authorization: String = "",
        userAgent: String = "",
        attributes: MutableMap<String, Any> = HashMap(),
    ) {
        this.scheme = scheme
        this.host = host
        this.origin = origin
        this.ip = ip
        this.uri = uri
        this.traceId = traceId
        this.requestId = requestId
        this.language = language
        this.authorization = authorization
        this.userAgent = userAgent
        this.attributes = attributes
    }

    val scheme: String
    val host: String
    val origin: String
    val ip: String
    val uri: String
    val traceId: String
    val requestId: String
    val language: String
    val authorization: String
    val userAgent: String
    val attributes: MutableMap<String, Any>

    fun putAttribute(key: String, value: Any) {
        this.attributes.put(key, value)
    }

    fun <T> getAttribute(key: String): T {
        return this.attributes[key] as T
    }

}
