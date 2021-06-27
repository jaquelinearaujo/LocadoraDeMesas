package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Produto;
import com.dawii.trabfinal.models.filter.ProdutoFilter;
import com.dawii.trabfinal.models.response.ProdutoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProdutoService {
    public ProdutoResponse insertProduto(Produto request);
    public ProdutoResponse buscarTodos();
    public ProdutoResponse buscarProdutoPorId(Produto request);
    public ProdutoResponse apagarProduto(Produto request);
    public ProdutoResponse editarProduto(Produto request);
    public Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable);
}
