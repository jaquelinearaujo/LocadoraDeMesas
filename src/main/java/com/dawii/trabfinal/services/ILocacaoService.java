package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;

public interface ILocacaoService {
    public LocacaoResponse insertLocacao(LocacaoRequest request);
    public LocacaoResponse buscarTodos();
    public LocacaoResponse buscarLocacaoPorId(Locacao request);
    public LocacaoResponse apagarLocacao(Locacao request);
    public LocacaoResponse editarLocacao(Locacao request);
}
