package com.dawii.trabfinal.controllers;

import com.dawii.trabfinal.models.request.LocacaoRequest;
import com.dawii.trabfinal.models.response.LocacaoResponse;
import com.dawii.trabfinal.services.ILocacaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locacao")
@AllArgsConstructor
@Data
public class LocacaoController {
    private final ILocacaoService locacaoService;

    @PostMapping()
    public ResponseEntity<LocacaoResponse> insertLocacao(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().insertLocacao(request));
    }

    @GetMapping
    public ResponseEntity<LocacaoResponse> buscarTodos() {
        return ResponseEntity.ok(getLocacaoService().buscarTodos());
    }

    @GetMapping("/mesaId")
    public ResponseEntity<LocacaoResponse> buscarLocacaoPorMarca(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().buscarMesaPorId(request));
    }

    @GetMapping("/pessoaId")
    public ResponseEntity<LocacaoResponse> buscarLocacaoPorPessoa(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().buscarPessoaPorId(request));
    }

    @GetMapping("/id")
    public ResponseEntity<LocacaoResponse> buscarLocacao(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().buscarLocacaoPorId(request));
    }

    @DeleteMapping
    public ResponseEntity<LocacaoResponse> apagarLocacao(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().apagarLocacao(request));
    }

    @PutMapping
    public ResponseEntity<LocacaoResponse> editarLocacao(@RequestBody LocacaoRequest request) {
        return ResponseEntity.ok(getLocacaoService().editarLocacao(request));
    }
}
