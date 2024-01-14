package br.com.walterpaulo.demo.damain.config.factorymethod;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTPClientFactory {

	@Value("${ftp.servidor}")
	private String servidor;
	@Value("${ftp.usuario}")
	private String usuario;
	@Value("${ftp.porta}")
	private int porta;
	@Value("${ftp.senha}")
	private String senha;
	private FTPClient ftpClient;

	private FTPClientFactory() {
		this.ftpClient = this.createFTPClient(true);
	}

	public FTPClient createFTPClient(boolean useFTPS) {
		FTPClientProvider provider;
		if (useFTPS) {
			provider = new FTPSProvider("TLS", true);
		} else {
			provider = new FTPProvider();
		}

		FTPClient ftpClient = provider.createFTPClient();

		return ftpClient;
	}

	public boolean connect() throws SocketException, IOException {

		try {
			ftpClient.setStrictReplyParsing(false);

			ftpClient.connect(this.servidor, porta);
			ftpClient.login(this.usuario, this.senha);
			// verifica se conectou com sucesso!
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				ftpClient.login(this.usuario, this.senha);
				disconnectFTP();
				return true;
			} else {
				// erro ao se conectar
				disconnectFTP();
				System.out.println("Conexão recusada");
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public void disconnectFTP() {
		try {
			this.ftpClient.logout();
			this.ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
