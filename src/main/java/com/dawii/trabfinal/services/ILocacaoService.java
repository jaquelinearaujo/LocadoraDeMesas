package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.response.LocacaoResponse;

public interface ILocacaoService {
    public LocacaoResponse insertLocacao(Locacao request);
    public LocacaoResponse buscarTodos();
    public LocacaoResponse buscarLocacaoPorId(Locacao request);
    public LocacaoResponse apagarLocacao(Locacao request);
    public LocacaoResponse editarLocacao(Locacao request);
}
