package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Papel;
import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IPapelRepository;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@AllArgsConstructor
@Data
public class PessoaController {
    private final IPessoaService service;

    @Autowired
    private IPapelRepository iPapelRepository;

    @RequestMapping(value = "/abririnserir", method = RequestMethod.GET)
    public ModelAndView abrirInserir(Pessoa pessoa) {
        ModelAndView mv = new ModelAndView("usuario/inserir");
        List<Papel> papeis = iPapelRepository.findAll();
        mv.addObject("todosPapeis", papeis);
        mv.addObject("usuario", pessoa);
        return mv;
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ModelAndView cadastrar(@Valid Pessoa request,
                                  BindingResult errors,
                                  RedirectAttributes attributes){
        ModelAndView mv = new ModelAndView();
        if(errors.hasErrors()) {
            mv = new ModelAndView("redirect:/api/usuario/abririnserir");
            attributes.addFlashAttribute("errors", errors.getFieldErrors());
            return mv;
        }else{
            PessoaResponse pessoaResponse = getService().salvar(request);
            if (!pessoaResponse.getMessages().isEmpty()){
                mv = new ModelAndView("redirect:/api/usuario/abririnserir");
                attributes.addFlashAttribute("mensagem", pessoaResponse.getMessages().get(0));
                return mv;
            }
            mv = new ModelAndView("redirect:/api/usuario/abririnserir");
            attributes.addFlashAttribute("mensagem", pessoaResponse.getMessages().get(0));
        }
        return mv;
    }

    @GetMapping("/abrirmostrartodos")
    public ModelAndView abrirMostrarTodos() {
        PessoaResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");

        if (!response.getMessages().isEmpty()){
            mv.addObject("mensagem", response.getMessages().get(0));
            return mv;
        }
        mv = new ModelAndView("usuario/mostrartodos");
        mv.addObject("usuarios", response.getPessoas());
        return mv;
    }
}
