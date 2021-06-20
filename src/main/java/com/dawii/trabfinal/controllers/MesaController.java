package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.request.MesaRequest;
import com.dawii.trabfinal.models.response.MesaResponse;
import com.dawii.trabfinal.services.IMesaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mesa")
@AllArgsConstructor
@Data
public class MesaController {
    private final IMesaService service;

    @PostMapping
    public ResponseEntity<MesaResponse> inserirMesa(@RequestBody MesaRequest request){
        MesaResponse response = getService().insertMesa(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<MesaResponse> buscarTodasAsMesas(){
        MesaResponse response = getService().buscarTodos();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/id")
    public ResponseEntity<MesaResponse> buscarMesaPorId(@RequestBody MesaRequest request){
        MesaResponse response = getService().buscarMesaPorId(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<MesaResponse> deletarMesaPorId(@RequestBody MesaRequest request){
        MesaResponse response = getService().apagarMesa(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping
    public ResponseEntity<MesaResponse> editarMesa(@RequestBody MesaRequest request){
        MesaResponse response = getService().editarMesa(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
