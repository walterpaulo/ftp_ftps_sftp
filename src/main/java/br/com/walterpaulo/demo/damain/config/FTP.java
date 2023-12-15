package br.com.walterpaulo.demo.damain.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTP {
	private String servidor;
	private String usuario;
	private int porta;
	private String senha;
	private FTPClient ftpClient;

	 public FTP(@Value("${ftp.servidor}") String servidor,
             @Value("${ftp.porta}") int porta,
             @Value("${ftp.usuario}") String usuario,
             @Value("${ftp.senha}") String senha) {
      this.servidor = servidor;
      this.porta = porta;
      this.usuario = usuario;
      this.senha = senha;
      this.ftpClient = new FTPClient();
  }

//	conectar no servidor
	public boolean connect() throws SocketException, IOException {

		try {
			ftpClient.connect(this.servidor, porta);

		} catch (Exception e) {
			// TODO: handle exception
		}
		// verifica se conectou com sucesso!
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			ftpClient.login(this.usuario, this.senha);
		} else {
			// erro ao se conectar
			disconnectFTP();
			System.out.println("Conexão recusada");
			System.exit(1);
			return false;
		}
		return true;
	}

//	enviar arquivo
	public boolean sendFTPFile(String caminhoArquivo, String arquivo) throws IOException {
		FileInputStream arqEnviar = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();
			arqEnviar = new FileInputStream(caminhoArquivo);
			if (ftpClient.storeFile(arquivo, arqEnviar)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			arqEnviar.close();
			disconnectFTP();
		}
		return false;
	}

//	baixar uma lista de arquivo
	public FTPFile[] getFTPFiles(String diretorio) {
		FTPFile[] filesConfig = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(diretorio);
			filesConfig = ftpClient.listFiles();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}
		return filesConfig;
	}

//	retornar nomes de arquivos no diretório
	public String[] getNameDirs(String diretorio) throws SocketException, IOException {
		String[] nameDirs = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(diretorio);
			nameDirs = ftpClient.listNames();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}
		return nameDirs;
	}

//	baixar arquivo
	public void getFile(String arquivo) {
		try {
			connect();
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			OutputStream os = new FileOutputStream(arquivo);
			ftpClient.retrieveFile(arquivo, os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}
	}

//	desconectar
	public void disconnectFTP() {
		try {
			this.ftpClient.logout();
			this.ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
