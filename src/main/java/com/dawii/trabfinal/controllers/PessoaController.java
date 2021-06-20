package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.request.PessoaRequest;
import com.dawii.trabfinal.models.response.PessoaResponse;
import com.dawii.trabfinal.services.IPessoaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
@AllArgsConstructor
@Data
public class PessoaController {
    private final IPessoaService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<PessoaResponse> cadastrar(@RequestBody PessoaRequest request){
        return ResponseEntity.ok(getService().cadastrarUsuario(request));
    }

    @PostMapping("/entrar")
    public ResponseEntity<PessoaResponse> entrar(@RequestBody PessoaRequest request){
        return ResponseEntity.ok(getService().entrar(request));
    }
}
