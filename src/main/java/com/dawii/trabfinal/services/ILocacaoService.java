package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;

public interface ILocacaoService {
    public LocacaoResponse insertLocacao(LocacaoRequest request);
    public LocacaoResponse buscarTodos();
    public LocacaoResponse buscarLocacaoPorId(LocacaoRequest request);
    public LocacaoResponse buscarMesaPorId(LocacaoRequest request);
    public LocacaoResponse buscarPessoaPorId(LocacaoRequest request);
    public LocacaoResponse apagarLocacao(LocacaoRequest request);
    public LocacaoResponse editarLocacao(LocacaoRequest request);
}
