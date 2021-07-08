package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.filter.LocacaoFilter;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILocacaoService {
    public LocacaoResponse insertLocacao(LocacaoRequest request);
    public LocacaoResponse buscarTodos();
    public LocacaoResponse buscarLocacaoPorId(Locacao request);
    public LocacaoResponse buscarLocacaoPorPessoaPorId(Long request);
    public LocacaoResponse apagarLocacao(Locacao request);
    public LocacaoResponse editarLocacao(Locacao request);
    public Page<Locacao> pesquisar(LocacaoFilter filtro, Pageable pageable);
}
