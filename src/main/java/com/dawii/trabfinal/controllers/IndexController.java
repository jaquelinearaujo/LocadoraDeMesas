package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.services.IProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	private final IProdutoService service;

	public IndexController(IProdutoService service) {
		this.service = service;
	}

	@GetMapping(value = {"/", "/index.html"} )
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");
		ProdutoResponse response = service.buscarTodos();
		mv.addObject("produtos", response.getProdutos());
		return mv;
	}

	@GetMapping(value = {"/login", "/login.html"} )
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}
}