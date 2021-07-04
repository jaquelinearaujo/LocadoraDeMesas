package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.repositories.IPessoaRepository;
import com.dawii.trabfinal.repositories.IProdutoRepository;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locacao")
@AllArgsConstructor
@Data
public class LocacaoController {
    private final ILocacaoService service;
    private final IProdutoService produtoService;

    @Autowired
    private IProdutoRepository produtoRepository;
    @Autowired
    private IPessoaRepository pessoaRepository;

    @GetMapping("/abririnserir")
    public ModelAndView abrirInserir(Locacao locacao) {
        ModelAndView mv = new ModelAndView("/locacao/inserir");
        List<Produto> produtos = produtoRepository.findAll();
        mv.addObject("produtos", produtos);
        mv.addObject("locacao", locacao);
        return mv;
    }

    @PostMapping(value = { "/cadastrar" })
    public ModelAndView inserirProduto(@Valid Locacao request, BindingResult result, Model model, RedirectAttributes atributos){
        getService().insertLocacao(request);

        ProdutoResponse response = getProdutoService().buscarTodos();
        ModelAndView mv = new ModelAndView();
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
