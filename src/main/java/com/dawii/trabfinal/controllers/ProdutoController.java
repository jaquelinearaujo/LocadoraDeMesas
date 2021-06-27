package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.filter.ProdutoFilter;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.pagination.PageWrapper;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/produto/")
@AllArgsConstructor
@Data
public class ProdutoController {
    private final IProdutoService service;

    @GetMapping("/abririnserir")
    public ModelAndView abrirInserir() {
        ModelAndView mv = new ModelAndView("/produto/inserir");
        mv.addObject("produto", new Produto());
        return mv;
    }

    @PostMapping(value = { "/cadastrar" })
    public ModelAndView inserirProduto(@Valid Produto request, BindingResult result, Model model){
        getService().insertProduto(request);

        ProdutoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("mensagem", "Produto cadastrado com sucesso");
        mv.addObject("produtos", response.getProdutos());
        return mv;
    }

    @PostMapping(value = { "/pesquisar" })
    public ModelAndView abrirMostrarTodos(ProdutoFilter request, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
        Page<Produto> pagina = getService().pesquisar(request, pageable);
        ModelAndView mv = new ModelAndView("produto/mostrartodos");
        PageWrapper<Produto> paginaWrapper = new PageWrapper<>(pagina, httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @GetMapping("/abrirpesquisa")
    public ModelAndView abrirPaginaPesquisa(ProdutoFilter produto) {
        ModelAndView mv = new ModelAndView("produto/pesquisar");
        return mv;
    }

    @PostMapping("/buscar")
    public ModelAndView buscarPeloCodigo(Produto request, ProdutoFilter produtoFilter) {
        ModelAndView mv = new ModelAndView();
        if (request != null) {
            ProdutoResponse response = getService().buscarProdutoPorId(request);
            mv.setViewName("produto/mostrar");
            response.getProdutos().stream().forEach(produto ->  mv.addObject("produto", produto));
        } else {
            mv.setViewName("produto/pesquisar");
            mv.addObject("mensagem", "Use um codigo válido");
        }
        return mv;
    }

    @PostMapping("/remover")
    @DeleteMapping
    public ModelAndView deletarProdutoPorId(Produto request){
        getService().apagarProduto(request);

        ProdutoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("mensagem", "Produto removido com sucesso");
        mv.addObject("produtos", response.getProdutos());
        return mv;
    }
    @PostMapping("/alterar")
    @PutMapping
    public ModelAndView editarProduto(Produto request){
        getService().editarProduto(request);

        ProdutoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("mensagem", "Produto alterado com sucesso");
        mv.addObject("produtos", response.getProdutos());
        return mv;
    }
}
