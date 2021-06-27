package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/usuario")
@AllArgsConstructor
@Data
public class PessoaController {
    private final IPessoaService service;

    @GetMapping("/abririnserir")
    public ModelAndView abrirFormInserir() {
        ModelAndView mv = new ModelAndView("usuario/inserir");
        mv.addObject("usuario", new Pessoa());
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView cadastrar(Pessoa request){
        getService().cadastrarUsuario(request);
        PessoaResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView("usuario/mostrartodos");
        mv.addObject("usuarios", response.getPessoas());
        return mv;
    }

    @GetMapping("/abrirmostrartodos")
    public ModelAndView abrirMostrarTodos() {
        PessoaResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView("usuario/mostrartodos");
        mv.addObject("usuarios", response.getPessoas());
        return mv;

    }

    @PostMapping("/login")
    public ResponseEntity<PessoaResponse> entrar(@RequestBody PessoaRequest request){
        return ResponseEntity.ok(getService().entrar(request));
    }
}
