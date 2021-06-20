package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;

public interface IPessoaService {
    public PessoaResponse cadastrarUsuario(PessoaRequest request);
    public PessoaResponse entrar(PessoaRequest request);
    public PessoaResponse buscarPessoaPorId(PessoaRequest request);
}
