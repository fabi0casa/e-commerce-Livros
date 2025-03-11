package com.fatec.livraria.controller;

import com.fatec.livraria.entity.TransacaoCliente;
import com.fatec.livraria.service.TransacaoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoClienteController {

    @Autowired
    private TransacaoClienteService transacaoClienteService;

    // Listar todas as transações
    @GetMapping("/all")
    public ResponseEntity<List<TransacaoCliente>> listarTodas() {
        return ResponseEntity.ok(transacaoClienteService.listarTodas());
    }

    // Buscar transação por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoCliente> buscarPorId(@PathVariable Integer id) {
        return transacaoClienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar transações de um cliente específico
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransacaoCliente>> buscarPorClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(transacaoClienteService.buscarPorClienteId(clienteId));
    }
}
