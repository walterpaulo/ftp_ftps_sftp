package br.com.walterpaulo.demo.damain.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Locale;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;

import org.apache.commons.net.ftp.FTPSClient;

public class FTPSClientWithSession extends FTPSClient {
	static {
		System.setProperty("jdk.tls.useExtendedMasterSecret", "false");
		System.setProperty("jdk.tls.client.enableSessionTicketExtension", "false");
	}

	public FTPSClientWithSession() {
		super();
	}

	public FTPSClientWithSession(String protocol, boolean portaPadrao) {
		super(protocol, portaPadrao);
	}

	@Override
	protected void _connectAction_() throws IOException {
		super._connectAction_();
		execPBSZ(0);
		execPROT("P");
//        ((SSLSocket) _socket_).setEnabledProtocols(new String[]{"TLSv1.2"});

	}

	@Override
	protected void _prepareDataSocket_(Socket socket) throws IOException {
		if (socket instanceof SSLSocket) {
			final SSLSession session = ((SSLSocket) _socket_).getSession();
			final SSLSessionContext context = session.getSessionContext();
			try {
				final Field sessionHostPortCache = context.getClass().getDeclaredField("sessionHostPortCache");
				sessionHostPortCache.setAccessible(true);
				final Object cache = sessionHostPortCache.get(context);
				final Method putMethod = cache.getClass().getDeclaredMethod("put", Object.class, Object.class);
				putMethod.setAccessible(true);
				final Method getHostMethod = socket.getClass().getDeclaredMethod("getHost");
				getHostMethod.setAccessible(true);
				Object host = getHostMethod.invoke(socket);
				final String key = String.format("%s:%s", host, String.valueOf(socket.getPort()))
						.toLowerCase(Locale.ROOT);
				putMethod.invoke(cache, key, session);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	}
}