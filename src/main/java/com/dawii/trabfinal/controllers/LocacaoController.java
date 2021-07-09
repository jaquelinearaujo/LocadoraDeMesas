package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.filter.LocacaoFilter;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.pagination.PageWrapper;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IPessoaService;
import com.dawii.trabfinal.services.IProdutoService;
import com.dawii.trabfinal.services.impl.ReportService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final IPessoaService pessoaService;

    @Autowired
    private ReportService relatorioService;

    @RequestMapping(value = "/abririnserir", method = RequestMethod.GET)
    public ModelAndView abrirInserir(LocacaoRequest request) {
        ModelAndView mv = new ModelAndView("/locacao/inserir");
        ProdutoResponse response = getProdutoService().buscarTodos();
        request.setLocacao(new Locacao());
        mv.addObject("produtos", response.getProdutos());
        mv.addObject("locacaoRequest", request);
        return mv;
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ModelAndView inserirProduto(@Valid LocacaoRequest locacaoRequest,
                                       BindingResult result,
                                       RedirectAttributes attributes){
        ModelAndView mv = new ModelAndView();
        if(result.hasErrors()) {
            mv = new ModelAndView("redirect:/api/locacao/abririnserir");
            attributes.addFlashAttribute("errors", result.getFieldErrors());
            return mv;
        }else {
            LocacaoResponse locacaoResponse = getService().insertLocacao(locacaoRequest);
            if (!locacaoResponse.getMessages().isEmpty()){
                mv = new ModelAndView("redirect:/api/locacao/abririnserir");
                attributes.addFlashAttribute("mensagem", locacaoResponse.getMessages().get(0));
                return mv;
            }
            mv = new ModelAndView("redirect:/api/locacao/abririnserir");
            attributes.addFlashAttribute("mensagem", locacaoResponse.getMessages().get(0));
        }
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
        LocacaoResponse locacaoResponse = getService().apagarLocacao(request);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        if (!locacaoResponse.getMessages().isEmpty()){
            mv.addObject("mensagem", locacaoResponse.getMessages().get(0));
            return mv;
        }

        locacaoResponse = getService().buscarTodos();
        mv.addObject("mensagem", "Locacao removida com sucesso");
        mv.addObject("locacoes", locacaoResponse.getLocacoes());
        return mv;
    }

    @GetMapping("/mostrartodos")
    public ModelAndView mostrarTodasLocacoes(Locacao request){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        PessoaResponse pessoa = getPessoaService().buscarPessoaPorUser(user.getName());

        LocacaoResponse response = getService().buscarLocacaoPorPessoaPorId(pessoa.getPessoas().get(0).getCodigo());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/locacao/mostrar");
        mv.addObject("locacoes", response.getLocacoes());
        return mv;
    }

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorioSimplesTodasPessoas() {
        byte[] relatorio = relatorioService.gerarRelatorioComplexoTodasLocacoes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Locacoes.pdf")
                .body(relatorio);
    }
}
