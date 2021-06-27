package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.request.ItemRequest;
import com.dawii.trabfinal.models.response.ItemResponse;

public interface IItemService {
    public ItemResponse insertItem(ItemRequest request);
    public ItemResponse buscarTodos();
    public ItemResponse buscarItemPorId(ItemRequest request);
    public ItemResponse buscarLocacaoPorId(ItemRequest request);
}
