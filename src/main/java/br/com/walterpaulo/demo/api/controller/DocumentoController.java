package br.com.walterpaulo.demo.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walterpaulo.demo.api.response.DocumentoResponse;
import br.com.walterpaulo.demo.damain.service.DocumentoService;

@RestController
@RequestMapping("/")
public class DocumentoController {

	@Autowired
	DocumentoService documentoService;

	@GetMapping
	public String name() {
		documentoService.enviarArquivo();
		return "ok";
	}

	@GetMapping("/enviar")
	public boolean enviar() {
		return documentoService.enviarArquivo();
	}

	@GetMapping("/listar")
	public List<DocumentoResponse> listar() {
		return documentoService.listarArquivos();
	}

	@GetMapping("/conectar")
	public DocumentoResponse conectar() {
		return documentoService.connectarFTPS();

	}

	@GetMapping("/conectarFactory")
	public DocumentoResponse conectarFactory() {
		return documentoService.connectarFTPFactory();

	}
}
