package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.request.HistoricoRequest;
import com.dawii.trabfinal.models.response.HistoricoResponse;

public interface IHistoricoService {
    public HistoricoResponse insertHistorico(HistoricoRequest request);
    public HistoricoResponse buscarTodos();
    public HistoricoResponse buscarHistoricoPorId(HistoricoRequest request);
    public HistoricoResponse buscarLocacaoPorId(HistoricoRequest request);
}
