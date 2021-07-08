package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Item;
import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.filter.LocacaoFilter;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IItemRepository;
import com.dawii.trabfinal.repositories.ILocacaoRepository;
import com.dawii.trabfinal.repositories.pagination.PaginacaoUtil;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IPessoaService;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class LocacaoService implements ILocacaoService, Serializable {
    @PersistenceContext
    private EntityManager manager;

    private final ILocacaoRepository repository;
    private final IPessoaService pessoaService;
    private final IProdutoService produtoService;
    private final IItemRepository itemRepository;

    @Override
    public LocacaoResponse insertLocacao(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null
                || request.getLocacao() == null
                || request.getLocacao().getDataInicio() == null
                || request.getLocacao().getDataFim() == null
                || request.getLocacao().getProdutos() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para Locacao estão presentes e corretos");
            return response;
        }else{
            try {
                Authentication user = SecurityContextHolder.getContext().getAuthentication();
                PessoaResponse pessoa = getPessoaService().buscarPessoaPorUser(user.getName());

                Locacao locacao = request.getLocacao();
                locacao.setCodPessoa(pessoa.getPessoas().get(0).getCodigo());
                locacao.setValTotal(0f);

                List<Item> items = new ArrayList<>();
                List<Integer> quantidades = request.getQuantidades().stream().filter(i -> i != null).collect(Collectors.toList());
                if (quantidades.size() != locacao.getProdutos().size()){
                    applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que o item esteja selecionado para passar uma quantidade deste");
                    return response;
                }
                for (Produto p: locacao.getProdutos()) {
                    Item item = new Item();
                    item.setCodigoProduto(p.getCodigo());
                    Integer produtoIndex = request.getLocacao().getProdutos().indexOf(p);
                    item.setQuantidade(quantidades.get(produtoIndex));

                    Integer estoque = p.getEstoqueAtual() - item.getQuantidade();
                    if (estoque < 0){
                        applyErrorMessage(Status.FAIL, response, "Selecione uma quantidade de estoque que não ultrapasse o estoque atual");
                        return response;
                    }
                    p.setEstoqueAtual(estoque);
                    getProdutoService().editarProduto(p);

                    locacao.setValTotal(locacao.getValTotal() + (item.getQuantidade() * p.getPreco()));
                    items.add(item);
                }

                locacao = getRepository().save(locacao);
                final Long codLocacao = locacao.getCodigo();
                items.stream().forEach(item -> item.setCodigoLocacao(codLocacao));
                items.stream().forEach(item -> getItemRepository().save(item));

                response.setLocacoes(Arrays.asList(locacao));
            }catch (Exception e){
                applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao nao cadastrada");
            }
        }

        return response;
    }

    @Override
    public LocacaoResponse buscarTodos() {
        LocacaoResponse response = new LocacaoResponse();
        List<Locacao> locacoes = getRepository().findAll();

        if (locacoes == null || locacoes.isEmpty()){
            response.getMessages().add("Não existem locacoes cadastradas");
            return response;
        }

        for (Locacao locacao: locacoes) {
            addProdutosLocacao(locacao);
        }

        response.setLocacoes(locacoes);

        return response;
    }

    @Override
    public LocacaoResponse buscarLocacaoPorId(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null || request.getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para Locacao estão presentes");
            return response;
        }

        Optional<Locacao> locacaoOptional = getRepository().findById(request.getCodigo());
        if (!locacaoOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Locacao requisitada não existe");
            return response;
        }

        Locacao locacao = locacaoOptional.get();
        addProdutosLocacao(locacao);

        response.setLocacoes(Arrays.asList(locacao));
        return response;
    }

    @Override
    public LocacaoResponse buscarLocacaoPorPessoaPorId(Long request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null || request == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para locacao estão presentes");
            return response;
        }

        List<Locacao> locacoes = getRepository().findByCodPessoa(request);
        if (locacoes == null){
            applyErrorMessage(Status.FAIL,response,"Locacoes requisitadas não existem");
            return response;
        }

        for (Locacao locacao: locacoes) {
            addProdutosLocacao(locacao);
        }

        response.setLocacoes(locacoes);
        return response;
    }

    private void addProdutosLocacao(Locacao locacao) {
        List<Item> itemsLocacao = getItemRepository().findByCodigoLocacao(locacao.getCodigo());
        itemsLocacao.stream()
                .map(l -> getProdutoService().buscarProdutoPorId(new Produto(l.getCodigoProduto())))
                .forEach(produtoResponse -> locacao.getProdutos().addAll(produtoResponse.getProdutos()));

        for (Item item : itemsLocacao) {
            locacao.getProdutos().stream().filter(produto -> produto.getCodigo() == item.getCodigoProduto()).findFirst().get().setQuantidade(item.getQuantidade());
        }
    }

    @Override
    public LocacaoResponse apagarLocacao(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null || request.getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Locacao para ser removida estão presentes");
            return response;
        }

        response = buscarLocacaoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao requisitada para ser removida não existe");
            return response;
        }

        getRepository().deleteById(request.getCodigo());
        response.setLocacoes(Arrays.asList(response.getLocacoes().get(0)));
        applyErrorMessage(Status.SUCCESS,response,"Locacao removida");

        return response;
    }

    @Override
    public LocacaoResponse editarLocacao(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null
                || request.getDataInicio() == null
                || request.getDataFim() == null
                || request.getProdutos() == null
                || request.getCodPessoa() == null
                || request.getValTotal() == null){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Locacao para ser editada estão presentes");
            return response;
        }

        response = buscarLocacaoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao requisitada para ser editada não existe");
            return response;
        }

        response.setLocacoes(Arrays.asList(getRepository().save(request)));

        return response;
    }

    @Override
    public Page<Locacao> pesquisar(LocacaoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Locacao> criteriaQuery = builder.createQuery(Locacao.class);
        Root<Locacao> f = criteriaQuery.from(Locacao.class);
        TypedQuery<Locacao> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();
        Predicate[] predicateArray;

        if (filtro.getDataInicio() != null) {
            predicateList.add(builder.equal(f.<String>get("dataInicio"), filtro.getDataInicio()));
        }

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        PessoaResponse pessoa = getPessoaService().buscarPessoaPorUser(user.getName());
        filtro.setUser(pessoa.getPessoas().get(0).getCodigo());


        if (filtro.getUser() != null) {
            predicateList.add(builder.equal(f.<Long>get("codPessoa"), filtro.getUser()));
        }

        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(f.<Long>get("codigo"), filtro.getCodigo()));
        }

        predicateArray = new Predicate[predicateList.size()];
        predicateList.toArray(predicateArray);

        PaginacaoUtil.prepararOrdem(f, criteriaQuery, builder, pageable);

        criteriaQuery.select(f).where(predicateArray).distinct(true);
        typedQuery = manager.createQuery(criteriaQuery);
        typedQuery.setHint(QueryHints.PASS_DISTINCT_THROUGH, false);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Locacao> locacoes = typedQuery.getResultList();

        long totalRegistros = PaginacaoUtil.getTotalRegistros(f, predicateArray, builder, manager);

        Page<Locacao> pagina = new PageImpl<>(locacoes, pageable, totalRegistros);

        return pagina;
    }

    private void applyErrorMessage(Status status, LocacaoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }
}
