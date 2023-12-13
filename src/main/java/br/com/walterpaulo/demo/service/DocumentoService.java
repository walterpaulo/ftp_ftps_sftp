package br.com.walterpaulo.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import br.com.walterpaulo.demo.config.sftp.SftpSessionFactoryHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoService {

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

}
