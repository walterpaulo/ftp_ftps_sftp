package br.com.walterpaulo.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walterpaulo.demo.api.response.Response;
import br.com.walterpaulo.demo.damain.service.HomeService;

@RestController
@RequestMapping("/")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping
	public Response home() {
		return new Response(homeService.home());
	}

}
