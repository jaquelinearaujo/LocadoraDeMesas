package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.filter.LocacaoFilter;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.pagination.PageWrapper;
import com.dawii.trabfinal.services.ILocacaoService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value = { "/pesquisar" })
    public ModelAndView abrirMostrarTodos(LocacaoFilter request, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
        Page<Locacao> pagina = getService().pesquisar(request, pageable);
        ModelAndView mv = new ModelAndView("locacao/mostrarporusuario");
        PageWrapper<Locacao> paginaWrapper = new PageWrapper<>(pagina, httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @GetMapping("/abrirpesquisa")
    public ModelAndView abrirPaginaPesquisa(LocacaoFilter filter) {
        ModelAndView mv = new ModelAndView("locacao/pesquisar");
        return mv;
    }

    @PostMapping("/buscar")
    public ModelAndView buscarPeloCodigo(Locacao request, LocacaoFilter locacaoFilter) {
        ModelAndView mv = new ModelAndView();
        if (request != null) {
            LocacaoResponse response = getService().buscarLocacaoPorId(request);
            mv.setViewName("locacao/mostrar");
            response.getLocacoes().stream().forEach(locacao -> mv.addObject("locacao", locacao));
        } else {
            mv.setViewName("locacao/pesquisar");
            mv.addObject("mensagem", "Use um codigo válido");
        }
        return mv;
    }

    @PostMapping("/remover")
    @DeleteMapping
    public ModelAndView deletarLocacaoPorId(Locacao request){
        getService().apagarLocacao(request);

        LocacaoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("mensagem", "Locacao removida com sucesso");
        mv.addObject("locacoes", response.getLocacoes());
        return mv;
    }
    @PostMapping("/alterar")
    @PutMapping
    public ModelAndView editarLocacao(Locacao request){
        getService().editarLocacao(request);

        LocacaoResponse response = getService().buscarTodos();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("mensagem", "Locacao alterada com sucesso");
        mv.addObject("produtos", response.getLocacoes());
        return mv;
    }
}
