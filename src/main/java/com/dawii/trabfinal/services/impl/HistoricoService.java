package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Historico;
import com.dawii.trabfinal.models.Locacao;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.HistoricoRequest;
import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.HistoricoResponse;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.repositories.IHistoricoRepository;
import com.dawii.trabfinal.services.IHistoricoService;
import com.dawii.trabfinal.services.ILocacaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class HistoricoService implements IHistoricoService {
    private final IHistoricoRepository repository;
    private final ILocacaoService locacaoService;

    @Override
    public HistoricoResponse insertHistorico(HistoricoRequest request) {
        HistoricoResponse response = new HistoricoResponse();

        if (request.getHistorico() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para o histórico estão presentes");
            return response;
        }

        Historico historico = getRepository().save(request.getHistorico());
        response.setHistoricos(Arrays.asList(historico));
        return response;
    }

    @Override
    public HistoricoResponse buscarTodos() {
        HistoricoResponse response = new HistoricoResponse();
        List<Historico> historicos = getRepository().findAll();

        if (historicos == null || historicos.isEmpty()){
            response.getMessages().add("Não existem historicos cadastrados");
            return response;
        }
        response.setHistoricos(historicos);

        return response;
    }

    @Override
    public HistoricoResponse buscarHistoricoPorId(HistoricoRequest request) {
        HistoricoResponse response = new HistoricoResponse();

        if (request.getHistorico() == null || request.getHistorico().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para o histórico estão presentes");
            return response;
        }

        Optional<Historico> historicoOptional = getRepository().findById(request.getHistorico().getCodigo());
        if (!historicoOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Histórico requisitado não existe");
            return response;
        }

        response.setHistoricos(Arrays.asList(historicoOptional.get()));
        return response;
    }

    @Override
    public HistoricoResponse buscarLocacaoPorId(HistoricoRequest request) {
        HistoricoResponse response = new HistoricoResponse();

        if (request.getHistorico() == null || request.getHistorico().getCodLocacao() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para o historico estão presentes");
            return response;
        }
        LocacaoResponse locacaoResponse = buscarPorLocacao(request);

        if (locacaoResponse.getLocacoes() == null || locacaoResponse.getLocacoes().isEmpty()){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que a pessoa da locacao existe");
            response.getMessages().addAll(locacaoResponse.getMessages());
            return response;
        }

        List<Optional<Historico>> historicoOptionalList = getRepository().findByCodLocacao(request.getHistorico().getCodLocacao());
        if (historicoOptionalList == null || historicoOptionalList.isEmpty()){
            applyErrorMessage(Status.FAIL,response,"Locacoes requisitadas não existem");
            return response;
        }

        response.setHistoricos(historicoOptionalList.stream().map(p -> p.get()).collect(Collectors.toList()));
        return response;
    }

    private void applyErrorMessage(Status status, HistoricoResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }

    public LocacaoResponse buscarPorLocacao(HistoricoRequest request) {
        Locacao locacao = new Locacao();
        locacao.setCodigo(request.getHistorico().getCodLocacao());
        LocacaoRequest locacaoRequest = new LocacaoRequest();
        locacaoRequest.setLocacao(locacao);
        LocacaoResponse locacaoResponse = getLocacaoService().buscarLocacaoPorId(locacaoRequest);
        return locacaoResponse;
    }
}
