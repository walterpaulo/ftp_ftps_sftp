package br.com.walterpaulo.demo.damain.service;

import org.springframework.stereotype.Service;

@Service
public class HomeService {

	public String home() {
		return "Enviar Arquivo por FTP, FTPS ou SFTP - API";
	}
}
