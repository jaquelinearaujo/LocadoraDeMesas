package com.dawii.trabfinal.services;

import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.response.PessoaResponse;

public interface IPessoaService {
    public PessoaResponse salvar(Pessoa request);
    public PessoaResponse buscarTodos();
    public PessoaResponse buscarPessoaPorId(Pessoa request);
    public PessoaResponse buscarPessoaPorUser(String request);
}
