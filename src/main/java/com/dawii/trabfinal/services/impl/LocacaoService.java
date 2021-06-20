package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Mesa;
import com.dawii.trabfinal.models.Pessoa;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.request.MesaRequest;
import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.models.response.MesaResponse;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.repositories.ILocacaoRepository;
import com.dawii.trabfinal.services.ILocacaoService;
import com.dawii.trabfinal.services.IMesaService;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class LocacaoService implements ILocacaoService, Serializable {

    private final ILocacaoRepository repository;
    private final IMesaService mesaService;
    private final IPessoaService pessoaService;

    @Override
    public LocacaoResponse insertLocacao(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null
                || request.getLocacao().getDtInicioLocacao() == null
                || request.getLocacao().getDtFimLocacao() == null
                || request.getLocacao().getCodMesa() == null
                || request.getLocacao().getCodPessoa() == null
                || request.getLocacao().getValTotal() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para Locacao estão presentes");
            return response;
        }
        MesaResponse mesaResponse = buscarMesaResponse(request);

        if (mesaResponse.getMesas() == null || mesaResponse.getMesas().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a mesa do locacao exista");
            response.getMessages().addAll(mesaResponse.getMessages());
            return response;
        }

        Locacao locacao = getRepository().save(request.getLocacao());
        response.setLocacoes(Arrays.asList(locacao));
        return response;
    }

    @Override
    public LocacaoResponse buscarTodos() {
        LocacaoResponse response = new LocacaoResponse();
        List<Locacao> locacoes = getRepository().findAll();

        if (locacoes == null || locacoes.isEmpty()){
            response.getMessages().add("Não existem locacoes cadastradas");
            return response;
        }
        response.setLocacoes(locacoes);

        return response;
    }

    @Override
    public LocacaoResponse buscarLocacaoPorId(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null || request.getLocacao().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para Locacao estão presentes");
            return response;
        }

        Optional<Locacao> locacaoOptional = getRepository().findById(request.getLocacao().getCodigo());
        if (!locacaoOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Locacao requisitada não existe");
            return response;
        }

        response.setLocacoes(Arrays.asList(locacaoOptional.get()));
        return response;
    }

    @Override
    public LocacaoResponse buscarMesaPorId(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null || request.getLocacao().getCodMesa() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para locacao estão presentes");
            return response;
        }
        MesaResponse marcaResponse = buscarMesaResponse(request);

        if (marcaResponse.getMesas() == null || marcaResponse.getMesas().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a mesa da lcoacao existe");
            response.getMessages().addAll(marcaResponse.getMessages());
            return response;
        }

        List<Optional<Locacao>> locacaoOptionalList = getRepository().findByCodMesa(request.getLocacao().getCodMesa());
        if (locacaoOptionalList == null || locacaoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Patrimonios requisitados não existem");
            return response;
        }

        response.setLocacoes(locacaoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }

    @Override
    public LocacaoResponse buscarPessoaPorId(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null || request.getLocacao().getCodMesa() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para locacao estão presentes");
            return response;
        }
        PessoaResponse pessoaResponse = buscarPessoaResponse(request);

        if (pessoaResponse.getPessoas() == null || pessoaResponse.getPessoas().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a pessoa da locacao existe");
            response.getMessages().addAll(pessoaResponse.getMessages());
            return response;
        }

        List<Optional<Locacao>> locacaoOptionalList = getRepository().findByCodMesa(request.getLocacao().getCodMesa());
        if (locacaoOptionalList == null || locacaoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Locacoes requisitadas não existem");
            return response;
        }

        response.setLocacoes(locacaoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }

    @Override
    public LocacaoResponse apagarLocacao(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null || request.getLocacao().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Locacao para ser removida estão presentes");
            return response;
        }

        response = buscarLocacaoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao requisitada para ser removida não existe");
            return response;
        }

        getRepository().deleteById(request.getLocacao().getCodigo());
        response.setLocacoes(Arrays.asList(response.getLocacoes().get(0)));
        applyErrorMessage(Status.SUCCESS,response,"Locacao removida");

        return response;
    }

    @Override
    public LocacaoResponse editarLocacao(LocacaoRequest request) {
        LocacaoResponse response = new LocacaoResponse();

        if (request.getLocacao() == null
                || request.getLocacao().getCodigo() == null
                || request.getLocacao().getCodMesa() == null
                || request.getLocacao().getCodPessoa() == null
                || request.getLocacao().getDtInicioLocacao() == null
                || request.getLocacao().getDtInicioLocacao() == null
                || request.getLocacao().getValTotal() == null){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Locacao para ser editada estão presentes");
            return response;
        }

        response = buscarLocacaoPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Locacao requisitada para ser editada não existe");
            return response;
        }

        MesaResponse mesaResponse = buscarMesaResponse(request);

        if (mesaResponse.getMesas() == null || mesaResponse.getMesas().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a mesa da Locacao existe");
            response.getMessages().addAll(mesaResponse.getMessages());
            return response;
        }

        response.setLocacoes(Arrays.asList(getRepository().save(request.getLocacao())));

        return response;
    }

    private void applyErrorMessage(Status status, LocacaoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }

    private MesaResponse buscarMesaResponse(LocacaoRequest request) {
        Mesa mesa = new Mesa();
        mesa.setCodigo(request.getLocacao().getCodMesa());
        MesaRequest mesaRequest = new MesaRequest();
        mesaRequest.setMesa(mesa);
        MesaResponse mesaResponse = getMesaService().buscarMesaPorId(mesaRequest);
        return mesaResponse;
    }

    private PessoaResponse buscarPessoaResponse(LocacaoRequest request) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(request.getLocacao().getCodMesa());
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setPessoa(pessoa);
        PessoaResponse pessoaResponse = getPessoaService().buscarPessoaPorId(pessoaRequest);
        return pessoaResponse;
    }
}
