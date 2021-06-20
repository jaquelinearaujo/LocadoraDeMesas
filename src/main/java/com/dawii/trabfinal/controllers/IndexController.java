package com.dawii.trabfinal.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@GetMapping(value = {"/", "/index.html"} )
	public ModelAndView index() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("index");
		//Se voce precisar pode inserir outros objetos no model para que sejam usados
		// na view index.html
		//mv.addObject("nome", valor);
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
}