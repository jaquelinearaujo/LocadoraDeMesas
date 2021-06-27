package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;

public interface IPessoaService {
    public PessoaResponse cadastrarUsuario(Pessoa request);
    public PessoaResponse entrar(PessoaRequest request);
    public PessoaResponse buscarTodos();
    public PessoaResponse buscarPessoaPorId(Pessoa request);
}
