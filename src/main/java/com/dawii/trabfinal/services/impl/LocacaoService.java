package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Item;
import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IItemRepository;
import com.dawii.trabfinal.repositories.ILocacaoRepository;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IPessoaService;
import com.dawii.trabfinal.services.IProdutoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Data
@AllArgsConstructor
public class LocacaoService implements ILocacaoService, Serializable {

    private final ILocacaoRepository repository;
    private final IPessoaService pessoaService;
    private final IItemRepository itemRepository;
    private final IProdutoService produtoService;

    @Override
    public LocacaoResponse insertLocacao(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request == null
                || request.getLocacao() == null
                || request.getLocacao().getDataInicio() == null
                || request.getLocacao().getDataFim() == null
                || request.getLocacao().getProdutos() == null
                || request.getLocacao().getProdutos().size() != request.getQuantidades().size()){
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
                for (Produto p: locacao.getProdutos()) {
                    Item item = new Item();
                    item.setCodigoProduto(p.getCodigo());
                    Integer produtoIndex = request.getLocacao().getProdutos().indexOf(p);
                    item.setQuantidade(request.getQuantidades().get(produtoIndex));

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

    private void applyErrorMessage(Status status, LocacaoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }
}
