package live.lingting.spring.web.scope

import java.util.Locale
import live.lingting.framework.http.header.HttpHeaders
import live.lingting.framework.time.DateTime

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
        headers: HttpHeaders = HttpHeaders.empty(false),
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
        this.headers = headers
        this.attributes = attributes
    }

    val time = DateTime.current()
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
    val headers: HttpHeaders
    val attributes: MutableMap<String, Any>

    fun putAttribute(key: String, value: Any) {
        this.attributes.put(key, value)
    }

    fun <T> getAttribute(key: String): T {
        return this.attributes[key] as T
    }

    fun language(): Locale? {
        try {
            if (language.isBlank()) {
                return null
            }
            return Locale.forLanguageTag(language)
        } catch (_: Exception) {
            return null
        }
    }

}
