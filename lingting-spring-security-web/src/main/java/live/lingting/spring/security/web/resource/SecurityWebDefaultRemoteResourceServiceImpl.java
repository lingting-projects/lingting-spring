package live.lingting.spring.security.web.resource;

import live.lingting.framework.http.HttpDelegateClient;
import live.lingting.framework.security.convert.SecurityConvert;
import live.lingting.framework.security.domain.AuthorizationVO;
import live.lingting.framework.security.domain.SecurityScope;
import live.lingting.framework.security.domain.SecurityToken;
import live.lingting.framework.security.resource.SecurityResourceService;
import live.lingting.framework.util.StringUtils;
import live.lingting.spring.security.web.properties.SecurityWebProperties;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;

/**
 * @author lingting 2024-03-21 19:41
 */
public class SecurityWebDefaultRemoteResourceServiceImpl implements SecurityResourceService {

	protected final HttpDelegateClient<?> client;

	protected final URI urlResolve;

	protected final SecurityConvert convert;

	protected final SecurityWebProperties properties;

	public SecurityWebDefaultRemoteResourceServiceImpl(String host, HttpDelegateClient<?> client,
			SecurityConvert convert, SecurityWebProperties properties) {
		this.client = client;
		this.urlResolve = URI.create(join(host, "authorization/resolve"));
		this.convert = convert;
		this.properties = properties;
	}

	public HttpRequest.Builder resolveBuilder(SecurityToken token) {
		HttpRequest.Builder builder = HttpRequest.newBuilder();
		builder.GET().uri(urlResolve);
		builder = fillSecurity(builder, token);
		return builder;
	}

	@SneakyThrows
	@Override
	public SecurityScope resolve(SecurityToken token) {
		HttpRequest.Builder builder = resolveBuilder(token);
		AuthorizationVO vo = client.request(builder.build(), AuthorizationVO.class);
		return convert.voToScope(vo);
	}

	protected HttpRequest.Builder fillSecurity(HttpRequest.Builder builder, SecurityToken token) {
		builder.header(properties.getHeaderAuthorization(), token.getRaw());
		return builder;
	}

	protected String join(String host, String uri) {
		if (!StringUtils.hasText(host)) {
			throw new IllegalArgumentException("remoteHost is not Null!");
		}
		StringBuilder builder = new StringBuilder(host);
		if (!host.startsWith("http")) {
			builder.append("http").append("://");
		}

		if (!host.endsWith("/")) {
			builder.append("/");
		}

		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		builder.append(uri);
		return builder.toString();
	}

}
