package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.filter.ProdutoFilter;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.repositories.IProdutoRepository;
import com.dawii.trabfinal.repositories.pagination.PaginacaoUtil;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Data
@AllArgsConstructor
public class ProdutoService implements IProdutoService {
    @PersistenceContext
    private EntityManager manager;

    private final IProdutoRepository repository;

    @Override
    public ProdutoResponse insertProduto(Produto request) {
        ProdutoResponse response = new ProdutoResponse();

        if (request.getNome() == null
                || request.getDescricao() == null
                || request.getPreco() == null
                || request.getEstoque() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para produto estão presentes");
            return response;
        }else{
            request.setEstoqueAtual(request.getEstoque());
            getRepository().save(request);
            response.setProdutos(Arrays.asList(request));
        }
        return response;
    }

    @Override
    public ProdutoResponse buscarTodos() {
        ProdutoResponse response = new ProdutoResponse();
        List<Produto> produtos = getRepository().findAll();

        if (produtos == null || produtos.isEmpty()){
            response.getMessages().add("Não existem produtos cadastrados");
            return response;
        }
        response.setProdutos(produtos);

        return response;
    }

    @Override
    public ProdutoResponse buscarProdutoPorId(Produto request) {
        ProdutoResponse response = new ProdutoResponse();

        if (request.getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para produto estão presentes");
            return response;
        }

        Optional<Produto> ProdutoOptional = getRepository().findById(request.getCodigo());
        if (!ProdutoOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Produto requisitada não existe");
            return response;
        }

        response.setProdutos(Arrays.asList(ProdutoOptional.get()));
        return response;
    }

    @Override
    public ProdutoResponse apagarProduto(Produto request) {
        ProdutoResponse response = new ProdutoResponse();

        if (request.getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de produto para ser removido estão presentes");
            return response;
        }

        response = buscarProdutoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Produto requisitada para ser removida não existe");
            return response;
        }

        getRepository().deleteById(request.getCodigo());
        response.setProdutos(Arrays.asList(response.getProdutos().get(0)));
        applyErrorMessage(Status.SUCCESS,response,"Produto removida");

        return response;
    }

    @Override
    public ProdutoResponse editarProduto(Produto request) {
        ProdutoResponse response = new ProdutoResponse();

        if (request.getNome() == null
                || request.getDescricao() == null
                || request.getPreco() == null
                || request.getEstoque() == null){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Produto para ser editada estão presentes");
            return response;
        }

        response = buscarProdutoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Produto requisitada para ser editada não existe");
            return response;
        }

        // Applying possible 'estoque' changes
        if (request.getEstoqueAtual() == null && request.getEstoque() == response.getProdutos().get(0).getEstoque()) {
            request.setEstoqueAtual(response.getProdutos().get(0).getEstoqueAtual());
        } else if (request.getEstoqueAtual() == null && request.getEstoque() != response.getProdutos().get(0).getEstoque()) {
            Integer estoqueAtual = request.getEstoque() - response.getProdutos().get(0).getEstoque() + response.getProdutos().get(0).getEstoqueAtual();
            request.setEstoqueAtual(estoqueAtual >= 0 ? estoqueAtual : 0);
        }
        response.setProdutos(Arrays.asList(getRepository().save(request)));

        return response;
    }

    @Override
    public Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
        Root<Produto> f = criteriaQuery.from(Produto.class);
        TypedQuery<Produto> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();
        Predicate[] predicateArray;

        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(f.<Long>get("codigo"), filtro.getCodigo()));
        }
        if (filtro.getNome() != null) {
            predicateList.add(builder.like(f.<String>get("nome"), "%" + filtro.getNome() + "%"));
        }

        predicateArray = new Predicate[predicateList.size()];
        predicateList.toArray(predicateArray);

        PaginacaoUtil.prepararOrdem(f, criteriaQuery, builder, pageable);

        criteriaQuery.select(f).where(predicateArray).distinct(true);
        typedQuery = manager.createQuery(criteriaQuery);
        typedQuery.setHint(QueryHints.PASS_DISTINCT_THROUGH, false);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Produto> Produtos = typedQuery.getResultList();

        long totalRegistros = PaginacaoUtil.getTotalRegistros(f, predicateArray, builder, manager);

        Page<Produto> pagina = new PageImpl<>(Produtos, pageable, totalRegistros);

        return pagina;
    }

    private void applyErrorMessage(Status status, ProdutoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }
}
