package br.com.walterpaulo.demo.damain.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import br.com.walterpaulo.demo.damain.config.SftpSessionFactoryHandler;
import br.com.walterpaulo.demo.api.response.DocumentoResponse;
import br.com.walterpaulo.demo.damain.config.FTP;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoService {

	@Autowired
	private FTP ftp;

	public void upload() {
		SftpSession session = new SftpSessionFactoryHandler().runFactory().getSession();
		InputStream resourceAsStream = DocumentoService.class.getClassLoader().getResourceAsStream("mytextfile.txt");
		try {
			String filename = String.format("mynewfile%s.txt", LocalDateTime.now());
			String destination = String.format("upload/beinguploaded/%s", filename);
			log.info("Write file to: " + destination);

			session.write(resourceAsStream, destination);
			String donedestination = String.format("upload/done/%s", filename);
			log.info("Rename file to: " + donedestination);
			session.rename(destination, donedestination);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		session.close();
	}

	public String download() {
		SftpSession session = new SftpSessionFactoryHandler().runFactory().getSession();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			session.read("upload/downloadme.txt", outputStream);
			return new String(outputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean enviarArquivo() {
		try {
			String nomeArquivo = "arquivo2.txt";
			String conteudo = "oi";

			criarArquivoTexto(nomeArquivo, conteudo);

			boolean enviado = ftp.sendFTPFile(nomeArquivo, nomeArquivo);
			FileInputStream fileInputStream = obterFileInputStream(nomeArquivo);

			if (fileInputStream != null) {
				fileInputStream.close();
			}
			this.removerArquivo(nomeArquivo);

			return enviado;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public List<DocumentoResponse> listarArquivos() {
		FTPFile[] arquivos = ftp.getFTPFiles("/");
		List<DocumentoResponse> documentos = new ArrayList<DocumentoResponse>();

		for (FTPFile ftpFile : arquivos) {
			DocumentoResponse novo = new DocumentoResponse(ftpFile.getName());
			documentos.add(novo);
		}
		return documentos;
	}

	public static void criarArquivoTexto(String nomeArquivo, String conteudo) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
			writer.write(conteudo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileInputStream obterFileInputStream(String nomeArquivo) {
		try {
			return new FileInputStream(new File(nomeArquivo));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean removerArquivo(String caminhoArquivoRemover) {
		File arquivoRemover = new File(caminhoArquivoRemover);
		if (arquivoRemover.exists()) {
			if (arquivoRemover.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
