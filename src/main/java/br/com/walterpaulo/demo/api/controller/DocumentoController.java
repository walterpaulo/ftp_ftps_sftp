package br.com.walterpaulo.demo.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walterpaulo.demo.api.response.Response;
import br.com.walterpaulo.demo.damain.service.DocumentoService;

@RestController
@RequestMapping("/documento")
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
	public List<Response> listar() {
		return documentoService.listarArquivos();
	}

	@GetMapping("/conectar")
	public Response conectar() {
		return documentoService.connectarFTPS();

	}

	@GetMapping("/conectarFactory")
	public Response conectarFactory() {
		return documentoService.connectarFTPFactory();

	}
}
