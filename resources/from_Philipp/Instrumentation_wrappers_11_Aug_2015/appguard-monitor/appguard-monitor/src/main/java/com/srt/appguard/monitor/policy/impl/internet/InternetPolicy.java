package com.srt.appguard.monitor.policy.impl.internet;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.srt.appguard.monitor.MonitorConfig;
import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.IntentPolicy;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class InternetPolicy extends IntentPolicy {

	public static final String EXCEPTION_MESSAGE = "Connection timed out";

	/* All Policies are required to be singletons. */
	public static InternetPolicy instance;

	static {
		instance = new InternetPolicy();
	}

	private Map<String, String> hosts = new HashMap<String, String>();

	public InternetPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.INTERNET;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void loadConfig(MonitorConfig config, boolean defaultValue) {
		super.loadConfig(config, defaultValue);
		hosts = config.getPermissionMetaInfo(android.Manifest.permission.INTERNET, Map.class, "hosts");
	}

	@Override
	protected boolean isIntentAllowed(Intent intent) {
		if (intent == null) {
			return true;
		}

		// TODO improve this
		final Uri uri = intent.getData();
		if (uri == null) {
			return true;
		}

		final String scheme = uri.getScheme();
		if (scheme == null) {
			return true;
		}

		if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme) || "javascript".equalsIgnoreCase(scheme)) {
			return isConnectionAllowed(uri.getHost());
		}

		return true;
	}

	protected boolean isConnectionAllowed(final String host) {
		if (host == null || host.isEmpty()) {
			return true;
		}

		// Check if this host is whitelisted/blacklisted, otherwise use value of INTERNET permission (default)
		boolean hostAllowed = allowed;
		if (hosts != null) {
			for (final Entry<String, String> entry : hosts.entrySet()) {
				if (host.equals(entry.getKey())) {
					String hostStatus = entry.getValue();
					if ("ALLOWED".equals(hostStatus)) {
						hostAllowed = true;
					} else if ("DENIED".equals(hostStatus)) {
						hostAllowed = false;
					}

					break;
				}
			}
		}

		if (hostAllowed) {
			Logger.allow(android.Manifest.permission.INTERNET, new Meta(MetaType.URI, host));
			return true;
		} else {
			Logger.deny(android.Manifest.permission.INTERNET, new Meta(MetaType.URI, host));
			return false;
		}
	}

	protected boolean isConnectionAllowed(final SocketAddress addr) {
		if (!(addr instanceof InetSocketAddress)) {
			return false;
		}

		final InetSocketAddress address = (InetSocketAddress) addr;
		return isConnectionAllowed(address.getHostName());
	}


	/* java.net.URL */
	@MapSignatures({
		"Ljava/net/URL;->openConnection()"
	})
	public void java_net_URL__openConnection(final URL url) throws Throwable {
		if (!isConnectionAllowed(url.getHost())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}


	/* java.net.URLConnection */
	// TODO JarURLConnection
	@MapSignatures({
		//"Ljava/net/URLConnection;->connect()",
		"Ljava/net/HttpURLConnection;->connect()"
		//"Ljava/net/URLConnection;->getInputStream()",
		//"Ljava/net/URLConnection;->getOutputStream()",
		//"Ljava/net/HttpURLConnection;->getInputStream()",
		//"Ljava/net/HttpURLConnection;->getOutputStream()"
	})
	public void java_net_URLConnection__connect(final URLConnection conn) throws Exception {
		if (!isConnectionAllowed(conn.getURL().getHost())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}


	/* org.apachet.http.impl.client.HttpClient */
	@MapSignatures({
		/*
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		*/
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
		/*
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
		*/

	})
	public void org_apache_http_impl_client_HttpClient__execute(final HttpClient _this, final HttpHost host) throws Exception {
		if (!isConnectionAllowed(host.getHostName())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
		/*
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/client/HttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		*/
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
		/*
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/AbstractHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)",
		
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)",
		"Lorg/apache/http/impl/client/ContentEncodingHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)"
		*/
	})
	public void org_apache_http_impl_client_HttpClient__execute(final HttpClient _this, final HttpUriRequest request) throws Exception {
		if (!isConnectionAllowed(request.getURI().getHost())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}


	/* java.net.Socket */
	// we only guard Socket constructors that create connected sockets
	// all other constructors are left unguarded, they will be checked when
	// Socket->connect() is called
	@MapSignatures({
		"Ljava/net/Socket;-><init>(Ljava/lang/String;I)",
		"Ljava/net/Socket;-><init>(Ljava/lang/String;ILjava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/lang/String;IZ)"
	})
	public void java_net_Socket_$init(final String host, final int port) throws Exception {
		if (!isConnectionAllowed(host)) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;ILjava/net/InetAddress;I)",
		"Ljava/net/Socket;-><init>(Ljava/net/InetAddress;IZ)"
	})
	public void java_net_Socket_$init(final InetAddress addr, final int port) throws Exception {
		if (!isConnectionAllowed(addr.getCanonicalHostName())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
		"Ljava/net/Socket;->connect(Ljava/net/SocketAddress;)",
		"Ljava/net/Socket;->connect(Ljava/net/SocketAddress;I)"
	})
	public void java_net_Socket__connect(final Socket _this, final SocketAddress addr) throws Exception {
		if (!isConnectionAllowed(addr)) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}


	/* java.net.DatagramSocket */
	// DatagramSocket constructors always create unconnected sockets
	// we only guard the connect(...) methods
	@MapSignatures({
		"Ljava/net/DatagramSocket;->connect(Ljava/net/SocketAddress;)"
	})
	public void java_net_DatagramSocket__connect(final DatagramSocket _this, final SocketAddress addr) throws Exception {
		if (!isConnectionAllowed(addr)) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
		"Ljava/net/DatagramSocket;->connect(Ljava/net/InetAddress;I)"
	})
	public void java_net_DatagramSocket__connect(final DatagramSocket _this, final InetAddress addr, final int port) throws Exception {
		if (!isConnectionAllowed(addr.getCanonicalHostName())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}


	/* java.net.MulticastSocket */
	// MulticastSocket constructos always create unconnected sockets
	// we only guard the joinGroup(...) methods
	@MapSignatures({
		"Ljava/net/MulticastSocket;->joinGroup(Ljava/net/SocketAddress;Ljava/net/NetworkInterface;)"
	})
	public void java_net_MulticastSocket__joinGroup(final MulticastSocket _this, final SocketAddress addr) throws Exception {
		if (!isConnectionAllowed(addr)) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
		"Ljava/net/MulticastSocket;->joinGroup(Ljava/net/InetAddress;)"
	})
	public void java_net_MulticastSocket__joinGroup(final MulticastSocket _this, final InetAddress addr) throws Exception {
		// TODO port
		if (!isConnectionAllowed(addr.getCanonicalHostName())) {
			throw new ConnectException(EXCEPTION_MESSAGE);
		}
	}

	@MapSignatures({
			"Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;)",
			"Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;Ljava/util/Map;)",
			"Landroid/webkit/WebView;->loadDataWithBaseURL(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)"
	})
	public void android_webkit_WebView__loadUrl(final WebView _this, final String url) throws Exception {
		if (url != null) {
			final String JAVASCRIPT_SCHEME = "javascript:";

			if (url.length() >= JAVASCRIPT_SCHEME.length() && JAVASCRIPT_SCHEME.equalsIgnoreCase(url.substring(0, JAVASCRIPT_SCHEME.length()))) {
				// NOP: JavaScript URL are always allowed. We assume users don't understand consequences of denying those URLs.
			} else {
				try {
					final URI uri = new URI(url);
					if (!isConnectionAllowed(uri.getHost())) {
						throw new MonitorException();
					}
				} catch (URISyntaxException e) {
					// NOP: Ignored and allow call!
				}
			}
		}
	}

	
	/* java.net.ServerSocket */
	// currently unguarded
	// TODO if we want to allow/deny this operation based on the remote addr,
	// we have to check the return value of the accept() method

	// "Ljava.net.ServerSocket.init()",
	// "Ljava.net.ServerSocket.init(int)",
	// "Ljava.net.ServerSocket.init(int,int)",
	// "Ljava.net.ServerSocket.init(int,int,java.net.InetAddress)",


	/* java.net.NetworkInterface */
	// currently unguarded. probably doesn't need to be guarded
	// "Ljava.net.NetworkInterface.init()",
	// "Ljava.net.NetworkInterface.init(java.lang.String,int,java.net.InetAddress)",
	
	
	/*
	// seems like this creates "unconnected" connections, so we don't need to guard this
	@MapSignatures({
		"Ljava/net/URLConnection;->init(Ljava/net/URL;)",
		"Ljava/net/HttpURLConnection;->init(Ljava/net/URL;)",
	})
	public void java_net_URLConnection_$init(final URL url) throws Throwable {
	}
	*/

	
	/* com.android.http.mulipart.* stuff */
	// the permission maps lists these, however, we probably don't need
	// to guard them as they need a connected output stream
	/*
	@MapSignatures({
		"com.android.http.multipart.Part.send(java.io.OutputStream)",
		"com.android.http.multipart.Part.sendParts(java.io.OutputStream,com.android.http.multipart.Part[])",
		"com.android.http.multipart.Part.sendParts(java.io.OutputStream,com.android.http.multipart.Part[],byte[])",
		"com.android.http.multipart.Part.sendStart(java.io.OutputStream)",
		"com.android.http.multipart.Part.sendTransferEncodingHeader(java.io.OutputStream)",
		"com.android.http.multipart.FilePart.sendData(java.io.OutputStream)",
		"com.android.http.multipart.FilePart.sendDispositionHeader(java.io.OutputStream)",
		"com.android.http.multipart.StringPart.sendData(java.io.OuputStream)",
	})
	public void com_android_http_multipart_$catchAll() {
	}
	*/
}
