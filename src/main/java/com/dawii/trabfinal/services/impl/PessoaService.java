package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IPessoaRepository;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Data
@AllArgsConstructor
public class PessoaService implements IPessoaService {
    private final IPessoaRepository repository;

    @Override
    public PessoaResponse cadastrarUsuario(PessoaRequest request) {
        return null;
    }

    @Override
    public PessoaResponse entrar(PessoaRequest request) {
        return null;
    }

    @Override
    public PessoaResponse buscarPessoaPorId(PessoaRequest request) {
        PessoaResponse response = new PessoaResponse();

        if (request.getPessoa() == null || request.getPessoa().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para mesa estão presentes");
            return response;
        }

        Optional<Pessoa> pessoaOptional = getRepository().findById(request.getPessoa().getCodigo());
        if (!pessoaOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Mesa requisitada não existe");
            return response;
        }

        response.setPessoas(Arrays.asList(pessoaOptional.get()));
        return response;
    }

    private void applyErrorMessage(Status status, PessoaResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }
}
