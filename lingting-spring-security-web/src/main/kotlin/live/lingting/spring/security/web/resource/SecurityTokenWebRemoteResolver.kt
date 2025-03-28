package live.lingting.spring.security.web.resource

import java.net.URI
import live.lingting.framework.Sequence
import live.lingting.framework.http.HttpClient
import live.lingting.framework.http.HttpRequest
import live.lingting.framework.security.convert.SecurityConvert
import live.lingting.framework.security.domain.AuthorizationVO
import live.lingting.framework.security.domain.SecurityScope
import live.lingting.framework.security.domain.SecurityToken
import live.lingting.framework.security.resolver.SecurityTokenResolver
import live.lingting.framework.util.StringUtils.hasText
import live.lingting.spring.security.web.properties.SecurityWebProperties

/**
 * @author lingting 2024-03-21 19:41
 */
open class SecurityTokenWebRemoteResolver(
    host: String,
    protected val client: HttpClient,
    protected val convert: SecurityConvert,
    protected val properties: SecurityWebProperties
) : SecurityTokenResolver, Sequence {
    protected val urlResolve: URI

    init {
        this.urlResolve = URI.create(join(host, "authorization/resolve"))
    }

    fun resolveBuilder(token: SecurityToken): HttpRequest.Builder {
        var builder = HttpRequest.builder()
        builder.get().url(urlResolve)
        builder = fillSecurity(builder, token)
        return builder
    }

    protected fun fillSecurity(builder: HttpRequest.Builder, token: SecurityToken): HttpRequest.Builder {
        builder.header(properties.headerAuthorization, token.raw)
        return builder
    }

    protected fun join(host: String, uri: String): String {
        var uri = uri
        require(hasText(host)) { "remoteHost is not Null!" }
        val builder = StringBuilder(host)
        if (!host.startsWith("http")) {
            builder.append("http").append("://")
        }

        if (!host.endsWith("/")) {
            builder.append("/")
        }

        if (uri.startsWith("/")) {
            uri = uri.substring(1)
        }
        builder.append(uri)
        return builder.toString()
    }

    override fun isSupport(token: SecurityToken?): Boolean {
        return true
    }

    override fun resolver(token: SecurityToken): SecurityScope {
        val builder = resolveBuilder(token)
        val request = builder.build()
        val vo = client.request(request, AuthorizationVO::class.java)
        return convert.voToScope(vo)
    }

    override val sequence: Int = Int.MAX_VALUE
}
