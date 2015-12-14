package com.srt.appguard.monitor.policy.impl.internet;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpsRedirectPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static HttpsRedirectPolicy instance;
	static {
		instance = new HttpsRedirectPolicy();
	}

	@Override
	protected String getPermissionName() {
		return "de.backessrt.appguard.permission.HttpsRedirect";
	}

	final Pattern[] patterns = new Pattern[] {};
	
	protected boolean isRedirected(String scheme, String host) {
		// fix case
		scheme = scheme.toLowerCase();
		host = host.toLowerCase();

		if (!allowed || "https".equals(scheme)) {
			return false;
		}
		
		final String url = scheme + "://" + host;
		for (final Pattern pattern : patterns) {
			final Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				Logger.info("Redirecting " + url);
				return true;
			}
		}

		return false;
	}

	protected URL isRedirected(final URL url) throws Exception {
		if (isRedirected(url.getProtocol(), url.getHost())) {
			return new URL("https", url.getHost(), url.getFile());
		}
		return null;
	}

	protected HttpHost isRedirected(final HttpHost host) {
		if (isRedirected(host.getSchemeName(), host.getHostName())) {
			return new HttpHost(host.getHostName(), host.getPort(), "https");
		}
		return null;
	}

	protected HttpUriRequest isRedirected(final HttpUriRequest request) throws Exception {
		final URI uri = request.getURI();
		if (isRedirected(uri.getScheme(), uri.getHost())) {
			// create wrapper object, overriding the getURI() method
			final URI redirect = new URI("https", uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
			return new HttpUriRequest() {
				@Override
				public URI getURI() {
					return redirect;
				}
				
				@Override
				public void setParams(HttpParams params) {
					request.setParams(params);
				}
				@Override
				public void setHeaders(Header[] headers) {
					request.setHeaders(headers);
				}
				@Override
				public void setHeader(String name, String value) {
					request.setHeader(name, value);
				}
				@Override
				public void setHeader(Header header) {
					request.setHeader(header);
				}
				@Override
				public void removeHeaders(String name) {
					request.removeHeaders(name);
				}
				@Override
				public void removeHeader(Header header) {
					request.removeHeader(header);
				}
				@Override
				public HeaderIterator headerIterator(String name) {
					return request.headerIterator(name);
				}
				@Override
				public HeaderIterator headerIterator() {
					return request.headerIterator();
				}
				@Override
				public ProtocolVersion getProtocolVersion() {
					return request.getProtocolVersion();
				}
				@Override
				public HttpParams getParams() {
					return request.getParams();
				}
				@Override
				public Header getLastHeader(String name) {
					return request.getLastHeader(name);
				}
				@Override
				public Header[] getHeaders(String name) {
					return request.getHeaders(name);
				}
				@Override
				public Header getFirstHeader(String name) {
					return request.getFirstHeader(name);
				}
				@Override
				public Header[] getAllHeaders() {
					return request.getAllHeaders();
				}
				@Override
				public boolean containsHeader(String name) {
					return request.containsHeader(name);
				}
				@Override
				public void addHeader(String name, String value) {
					request.addHeader(name, value);
				}
				@Override
				public void addHeader(Header header) {
					request.addHeader(header);
				}
				@Override
				public RequestLine getRequestLine() {
					return request.getRequestLine();
				}
				@Override
				public boolean isAborted() {
					return request.isAborted();
				}
				@Override
				public String getMethod() {
					return request.getMethod();
				}
				@Override
				public void abort() throws UnsupportedOperationException {
					request.abort();
				}
			};
		}
		return null;
	}
	

	/* java.net.URL */
	@MapSignatures({
		"Ljava/net/URL;->openConnection()"
	})
	public void java_net_URL__openConnection(URL url) throws Exception {
		final URL redirect = isRedirected(url);
		if (redirect != null) {
			throw new MonitorException(redirect.openConnection());
		}
	}

	/* org.apachet.http.impl.client.HttpClient */
	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)"
	})
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpHost host, HttpRequest request) throws Exception {
		final HttpHost redirect = isRedirected(host);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, request)); 
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)"
	})
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpHost host, HttpRequest request, HttpContext context) throws Exception {
		final HttpHost redirect = isRedirected(host);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, request, context)); 
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)"
	})
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpHost host, HttpRequest request, ResponseHandler handler) throws Exception {
		final HttpHost redirect = isRedirected(host);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, request, handler)); 
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
	})
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpHost host, HttpRequest request, ResponseHandler handler, HttpContext context) throws Exception {
		final HttpHost redirect = isRedirected(host);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, request, handler, context)); 
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)"
	})
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpUriRequest request) throws Exception {
		final HttpUriRequest redirect = isRedirected(request);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect));
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)"
	})
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpUriRequest request, HttpContext context) throws Exception {
		final HttpUriRequest redirect = isRedirected(request);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, context));
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)"
	})
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpUriRequest request, ResponseHandler handler) throws Exception {
		final HttpUriRequest redirect = isRedirected(request);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, handler));
		}
	}

	@MapSignatures({
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
	})
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void org_apache_http_impl_client_HttpClient__execute(HttpClient _this, HttpUriRequest request, ResponseHandler handler, HttpContext context) throws Exception {
		final HttpUriRequest redirect = isRedirected(request);
		if (redirect != null) {
			throw new MonitorException(_this.execute(redirect, handler, context));
		}
	}

	
	/*
	// we cannot handle this at the moment
	@MapSignatures({
		"Ljava/net/URLConnection;->init(Ljava/net/URL;)",
		"Ljava/net/HttpURLConnection;->init(Ljava/net/URL;)",
	})
	public void java_net_URLConnection_$init(final URL url) throws Throwable {
		log.debug("HttpsRedirectPolicy.java_net_URLConnection_$init(): " + url);
	}

	/* java.net.URLConnection * /
	// TODO JarURLConnection
	// we cannot handle this
	@MapSignatures({
		"Ljava/net/URLConnection;->connect()",
		"Ljava/net/HttpURLConnection;->connect()",
	})
	public void java_net_URLConnection__connect(final URLConnection conn) throws Exception {
		log.debug("HttpsRedirectPolicy.java_net_URLConnection__connect(): " + conn);
	}

	
	/* java.net.Socket * /
	// we only guard Socket constructors that create connected sockets
	// all other constructors are left unguarded, they will be checked when
	// Socket->connect() is called
	@MapSignatures({
		"Ljava/net/Socket;-><init>(Ljava/lang/String;I)",
		"Ljava/net/Socket;-><init>(Ljava/lang/String;ILjava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/lang/String;IZ)"
	})
	public void java_net_Socket_$init(final String host, final int port) throws Exception {
		log.debug("HttpsRedirectPolicy.java_net_Socket_$init(): " + host);
	}
	
	@MapSignatures({
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;ILjava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;IZ)"
	})
	public void java_net_Socket_$init(final InetAddress addr, final int port) throws Exception {
		log.debug("HttpsRedirectPolicy.java_net_Socket_$init(): " + addr);
	}

	@MapSignatures({
		"Ljava/net/Socket;->connect(Ljava/net/SocketAddress;)",
		"Ljava/net/Socket;->connect(Ljava/net/SocketAddress;I)"
	})
	public void java_net_Socket__connect(final Socket _this, final SocketAddress addr) throws Exception {
		log.debug("HttpsRedirectPolicy.java_net_Socket__connect(): " + addr);
	}
	*/
}
