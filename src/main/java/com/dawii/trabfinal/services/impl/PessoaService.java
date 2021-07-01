package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.IPessoaRepository;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Data
@AllArgsConstructor
public class PessoaService implements IPessoaService {
    private final IPessoaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PessoaResponse salvar(Pessoa request) {
        PessoaResponse response = new PessoaResponse();

        if (request == null
                || request.getEmail() == null
                || request.getNome() == null
                || request.getSenha() == null
                || request.getPapeis() == null
                || request.getUsuario() == null) {
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Verifique os dados do usuário a ser cadastrado");
            return response;
        } else {
            request.setNome(request.getNome().trim());
            request.setSenha((request.getSenha()));//TODO codificar senha

            if (getRepository().findByEmail(request.getEmail()).isPresent()){
                applyErrorMessage(Status.FAIL,response,"Email já cadastrado");
                return response;
            }

            try{
                request.setAtivo(true);
                request.setSenha(passwordEncoder.encode(request.getUsuario()));
                getRepository().save(request);
                response.getMessages().add("Usuario cadastrado com sucesso");
                response.setPessoas(Arrays.asList(request));
            } catch (Exception e){
                applyErrorMessage(Status.VALIDATION_ERROR,response,"Usuario nao cadastrado");
            }
        }
        return response;
    }

    @Override
    public PessoaResponse entrar(PessoaRequest request) {
        return null;
    }

    @Override
    public PessoaResponse buscarTodos() {
        PessoaResponse response = new PessoaResponse();
        List<Pessoa> pessoas = getRepository().findAll();

        if (pessoas == null || pessoas.isEmpty()){
            response.getMessages().add("Não existem pessoas cadastradas");
            return response;
        }
        response.setPessoas(pessoas);

        return response;
    }

    @Override
    public PessoaResponse buscarPessoaPorId(Pessoa request) {
        PessoaResponse response = new PessoaResponse();

        if (request == null || request.getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para mesa estão presentes");
            return response;
        }

        Optional<Pessoa> pessoaOptional = getRepository().findById(request.getCodigo());
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
