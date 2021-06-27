package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Locacao;
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
    public ModelAndView abrirInserir() {
        ModelAndView mv = new ModelAndView("/locacao/inserir");
        mv.addObject("locacao", new Locacao());

        ProdutoResponse produtoResponse = produtoService.buscarTodos();
        mv.addObject("produtos", produtoResponse);
        return mv;
    }

    @PostMapping(value = { "/cadastrar" })
    public ModelAndView inserirProduto(@Valid Locacao request, BindingResult result, Model model, RedirectAttributes atributos){
        getService().insertLocacao(request);

        LocacaoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/locacao/mostrarporusuario");
        mv.addObject("mensagem", "Locação cadastrada com sucesso");
        mv.addObject("locacoes", response.getLocacoes());
        return mv;
    }

    @GetMapping
    public ResponseEntity<LocacaoResponse> buscarTodos() {
        return ResponseEntity.ok(getService().buscarTodos());
    }

    @GetMapping("/produtoId")
    public ResponseEntity<LocacaoResponse> buscarLocacaoPorMarca(@RequestBody Locacao request) {
        return ResponseEntity.ok(getService().buscarProdutoPorId(request));
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
