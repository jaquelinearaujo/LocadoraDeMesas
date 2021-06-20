package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.request.HistoricoRequest;
import com.dawii.trabfinal.models.response.HistoricoResponse;
import com.dawii.trabfinal.services.IHistoricoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historico")
@AllArgsConstructor
@Data
public class HistoricoController {
    private final IHistoricoService service;

    @PostMapping
    public ResponseEntity<HistoricoResponse> inserirHistorico(@RequestBody HistoricoRequest request){
        HistoricoResponse response = getService().insertHistorico(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<HistoricoResponse> buscarTodosOsHistoricos(){
        HistoricoResponse response = getService().buscarTodos();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/id")
    public ResponseEntity<HistoricoResponse> buscarHistoricoPorId(@RequestBody HistoricoRequest request){
        HistoricoResponse response = getService().buscarHistoricoPorId(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/locacaoId")
    public ResponseEntity<HistoricoResponse> buscarHistoricoPorLocacao(@RequestBody HistoricoRequest request) {
        return ResponseEntity.ok(getService().buscarLocacaoPorId(request));
    }

}
