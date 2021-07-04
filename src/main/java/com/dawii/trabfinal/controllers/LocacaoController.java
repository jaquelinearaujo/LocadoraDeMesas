package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/locacao")
@AllArgsConstructor
@Data
public class LocacaoController {
    private final ILocacaoService service;
    private final IProdutoService produtoService;


    @GetMapping("/abririnserir")
    public ModelAndView abrirInserir(LocacaoRequest request) {
        ModelAndView mv = new ModelAndView("/locacao/inserir");
        ProdutoResponse response = getProdutoService().buscarTodos();
        request.setLocacao(new Locacao());
        mv.addObject("produtos", response.getProdutos());
        mv.addObject("locacaoRequest", request);
        return mv;
    }

    @PostMapping(value = { "/cadastrar" })
    public ModelAndView inserirProduto(@Valid LocacaoRequest locacaoRequest, BindingResult result, Model model, RedirectAttributes atributos){
        LocacaoResponse locacaoResponse = getService().insertLocacao(locacaoRequest);
        ProdutoResponse response = getProdutoService().buscarTodos();
        ModelAndView mv = new ModelAndView();

        if (!locacaoResponse.getMessages().isEmpty()){
            mv.setViewName("/index");
            mv.addObject("mensagem", locacaoResponse.getMessages().get(0));
            mv.addObject("produtos", response.getProdutos());
            return mv;
        }

        mv.setViewName("/index");
        mv.addObject("produtos", response.getProdutos());
        return mv;
    }

    @GetMapping
    public ResponseEntity<LocacaoResponse> buscarTodos() {
        return ResponseEntity.ok(getService().buscarTodos());
    }

    @GetMapping("/id")
    public ResponseEntity<LocacaoResponse> buscarLocacao(@RequestBody Locacao request) {
        return ResponseEntity.ok(getService().buscarLocacaoPorId(request));
    }

    @DeleteMapping
    public ResponseEntity<LocacaoResponse> apagarLocacao(@RequestBody Locacao request) {
        return ResponseEntity.ok(getService().apagarLocacao(request));
    }

    @PutMapping
    public ResponseEntity<LocacaoResponse> editarLocacao(@RequestBody Locacao request) {
        return ResponseEntity.ok(getService().editarLocacao(request));
    }
}
