package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import com.dawii.trabfinal.repositories.ILocacaoRepository;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IPessoaService;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class LocacaoService implements ILocacaoService, Serializable {

    private final ILocacaoRepository repository;
    private final IProdutoService ProdutoService;
    private final IPessoaService pessoaService;

    @Override
    public LocacaoResponse insertLocacao(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null
                || request.getDtInicioLocacao() == null
                || request.getDtFimLocacao() == null
                || request.getCodProduto() == null
                || request.getCodPessoa() == null
                || request.getValTotal() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para Locacao estão presentes");
            return response;
        }
        ProdutoResponse produtoResponse = buscarProdutoResponse(request);

        if (produtoResponse.getProdutos() == null || produtoResponse.getProdutos().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a Produto do locacao exista");
            response.getMessages().addAll(produtoResponse.getMessages());
            return response;
        }

        Locacao locacao = getRepository().save(request);
        response.setLocacoes(Arrays.asList(locacao));
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

        response.setLocacoes(Arrays.asList(locacaoOptional.get()));
        return response;
    }

    @Override
    public LocacaoResponse buscarProdutoPorId(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null || request.getCodProduto() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para locacao estão presentes");
            return response;
        }/*
        ProdutoResponse marcaResponse = buscarProdutoResponse(request);

        if (marcaResponse.getProdutos() == null || marcaResponse.getProdutos().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a Produto da lcoacao existe");
            response.getMessages().addAll(marcaResponse.getMessages());
            return response;
        }*/

        List<Optional<Locacao>> locacaoOptionalList = getRepository().findByCodProduto(request.getCodProduto());
        if (locacaoOptionalList == null || locacaoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Patrimonios requisitados não existem");
            return response;
        }

        response.setLocacoes(locacaoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }
/*
    @Override
    public LocacaoResponse buscarPessoaPorId(Locacao request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null || request.getCodProduto() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para locacao estão presentes");
            return response;
        }
        PessoaResponse pessoaResponse = buscarPessoaResponse(request);

        if (pessoaResponse == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a pessoa da locacao existe");
            response.getMessages().addAll(pessoaResponse.getMessages());
            return response;
        }

        List<Optional<Locacao>> locacaoOptionalList = getRepository().findByCodProduto(request.getCodProduto());
        if (locacaoOptionalList == null || locacaoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Locacoes requisitadas não existem");
            return response;
        }

        response.setLocacoes(locacaoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }*/

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
                || request.getCodigo() == null
                || request.getCodProduto() == null
                || request.getCodPessoa() == null
                || request.getDtInicioLocacao() == null
                || request.getDtInicioLocacao() == null
                || request.getValTotal() == null){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Locacao para ser editada estão presentes");
            return response;
        }

        response = buscarLocacaoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao requisitada para ser editada não existe");
            return response;
        }

        ProdutoResponse produtoResponse = buscarProdutoResponse(request);

        if (produtoResponse.getProdutos() == null || produtoResponse.getProdutos().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a Produto da Locacao existe");
            response.getMessages().addAll(produtoResponse.getMessages());
            return response;
        }

        response.setLocacoes(Arrays.asList(getRepository().save(request)));

        return response;
    }

    private void applyErrorMessage(Status status, LocacaoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }

    private ProdutoResponse buscarProdutoResponse(Locacao request) {
        Produto produto = new Produto();
        produto.setCodigo(request.getCodProduto());
        Produto produtoRequest = new Produto();
        ProdutoResponse produtoResponse = getProdutoService().buscarProdutoPorId(produtoRequest);
        return produtoResponse;
    }

    private PessoaResponse buscarPessoaResponse(Locacao request) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(request.getCodPessoa());
        PessoaResponse pessoaResponse = getPessoaService().buscarPessoaPorId(pessoa);
        return pessoaResponse;
    }
}
