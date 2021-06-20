package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.models.Mesa;
import com.dawii.trabfinal.models.Status;
import com.dawii.trabfinal.models.request.MesaRequest;
import com.dawii.trabfinal.models.response.MesaResponse;
import com.dawii.trabfinal.repositories.IMesaRepository;
import com.dawii.trabfinal.services.IMesaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Data
@AllArgsConstructor
public class MesaService implements IMesaService {
    private final IMesaRepository repository;

    @Override
    public MesaResponse insertMesa(MesaRequest request) {
        MesaResponse response = new MesaResponse();

        if (request.getMesa() == null
                || request.getMesa().getCor() == null
                || request.getMesa().getValorAluguel() == null
                || request.getMesa().getStatusLocacao() == null){
            applyErrorMessage(Status.VALIDATION_ERROR, response, "Certifique-se de que todos os campos para mesa estão presentes");
            return response;
        }

        Mesa mesa = getRepository().save(request.getMesa());
        response.setMesas(Arrays.asList(mesa));
        return response;
    }

    @Override
    public MesaResponse buscarTodos() {
        MesaResponse response = new MesaResponse();
        List<Mesa> mesas = getRepository().findAll();

        if (mesas == null || mesas.isEmpty()){
            response.getMessages().add("Não existem mesas cadastrados");
            return response;
        }
        response.setMesas(mesas);

        return response;
    }

    @Override
    public MesaResponse buscarMesaPorId(MesaRequest request) {
        MesaResponse response = new MesaResponse();

        if (request.getMesa() == null || request.getMesa().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos para mesa estão presentes");
            return response;
        }

        Optional<Mesa> mesaOptional = getRepository().findById(request.getMesa().getCodigo());
        if (!mesaOptional.isPresent()){
            applyErrorMessage(Status.FAIL,response,"Mesa requisitada não existe");
            return response;
        }

        response.setMesas(Arrays.asList(mesaOptional.get()));
        return response;
    }

    @Override
    public MesaResponse apagarMesa(MesaRequest request) {
        MesaResponse response = new MesaResponse();

        if (request.getMesa() == null || request.getMesa().getCodigo() == null ){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de mesa para ser removida estão presentes");
            return response;
        }

        response = buscarMesaPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Mesa requisitada para ser removida não existe");
            return response;
        }

        getRepository().deleteById(request.getMesa().getCodigo());
        response.setMesas(Arrays.asList(response.getMesas().get(0)));
        applyErrorMessage(Status.SUCCESS,response,"Mesa removida");

        return response;
    }

    @Override
    public MesaResponse editarMesa(MesaRequest request) {
        MesaResponse response = new MesaResponse();

        if (request.getMesa() == null
                || request.getMesa().getCodigo() == null
                || request.getMesa().getCor() == null
                || request.getMesa().getValorAluguel() == null
                || request.getMesa().getStatusLocacao() == null){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Certifique-se de que todos os campos de Mesa para ser editada estão presentes");
            return response;
        }

        response = buscarMesaPorId(request);
        if (!response.getStatus().equals(Status.SUCCESS)){
            applyErrorMessage(Status.VALIDATION_ERROR,response,"Mesa requisitada para ser editada não existe");
            return response;
        }

        response.setMesas(Arrays.asList(getRepository().save(request.getMesa())));

        return response;
    }

    private void applyErrorMessage(Status status, MesaResponse response, String message) {
        response.setStatus(status);
        response.getMessages().add(message);
    }
}
