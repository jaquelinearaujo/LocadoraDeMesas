package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.request.MesaRequest;
import com.dawii.trabfinal.models.response.MesaResponse;

public interface IMesaService {
    public MesaResponse insertMesa(MesaRequest request);
    public MesaResponse buscarTodos();
    public MesaResponse buscarMesaPorId(MesaRequest request);
    public MesaResponse apagarMesa(MesaRequest request);
    public MesaResponse editarMesa(MesaRequest request);
}
