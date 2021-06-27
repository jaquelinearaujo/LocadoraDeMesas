package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Item;
import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.ItemRequest;
import com.dawii.trabfinal.models.response.ItemResponse;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.repositories.IItemRepository;
import com.dawii.trabfinal.services.IItemService;
import com.dawii.trabfinal.services.ILocacaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class ItemService implements IItemService {
    private final IItemRepository repository;
    private final ILocacaoService locacaoService;

    @Override
    public ItemResponse insertItem(ItemRequest request) {
        ItemResponse response = new ItemResponse();

        if (request.getItem() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para o histórico estão presentes");
            return response;
        }

        Item item = getRepository().save(request.getItem());
        response.setItems(Arrays.asList(item));
        return response;
    }

    @Override
    public ItemResponse buscarTodos() {
        ItemResponse response = new ItemResponse();
        List<Item> items = getRepository().findAll();

        if (items == null || items.isEmpty()){
            response.getMessages().add("Não existem historicos cadastrados");
            return response;
        }
        response.setItems(items);

        return response;
    }

    @Override
    public ItemResponse buscarItemPorId(ItemRequest request) {
        ItemResponse response = new ItemResponse();

        if (request.getItem() == null || request.getItem().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para o histórico estão presentes");
            return response;
        }

        Optional<Item> historicoOptional = getRepository().findById(request.getItem().getCodigo());
        if (!historicoOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Histórico requisitado não existe");
            return response;
        }

        response.setItems(Arrays.asList(historicoOptional.get()));
        return response;
    }

    @Override
    public ItemResponse buscarLocacaoPorId(ItemRequest request) {
        ItemResponse response = new ItemResponse();

        if (request.getItem() == null || request.getItem().getCodLocacao() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para o historico estão presentes");
            return response;
        }
        LocacaoResponse locacaoResponse = buscarPorLocacao(request);

        if (locacaoResponse.getLocacoes() == null || locacaoResponse.getLocacoes().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a pessoa da locacao existe");
            response.getMessages().addAll(locacaoResponse.getMessages());
            return response;
        }

        List<Optional<Item>> historicoOptionalList = getRepository().findByCodLocacao(request.getItem().getCodLocacao());
        if (historicoOptionalList == null || historicoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Locacoes requisitadas não existem");
            return response;
        }

        response.setItems(historicoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }

    private void applyErrorMessage(Status status, ItemResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }

    public LocacaoResponse buscarPorLocacao(ItemRequest request) {
        Locacao locacao = new Locacao();
        locacao.setCodigo(request.getItem().getCodLocacao());
        LocacaoResponse locacaoResponse = getLocacaoService().buscarLocacaoPorId(locacao);
        return locacaoResponse;
    }
}
