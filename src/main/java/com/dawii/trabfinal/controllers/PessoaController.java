package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Papel;
import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IPapelRepository;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@AllArgsConstructor
@Data
public class PessoaController {
    private final IPessoaService service;

    @Autowired
    private IPapelRepository iPapelRepository;

    @GetMapping("/abririnserir")
    public ModelAndView abrirFormInserir(Pessoa pessoa) {
        ModelAndView mv = new ModelAndView("usuario/inserir");
        List<Papel> papeis = iPapelRepository.findAll();
        mv.addObject("todosPapeis", papeis);
        mv.addObject("usuario", pessoa);
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView cadastrar(Pessoa request){
        getService().salvar(request);
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
}
