package com.fatec.livraria.controller;

import com.fatec.livraria.dto.PedidoRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/all")
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Integer clienteId) {
        return pedidoService.listarPorClienteId(clienteId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequest request) {
        try {
            Pedido novoPedido = pedidoService.criarPedidoComVendas(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }

    @PatchMapping("/venda/{vendaId}/status")
    public ResponseEntity<?> atualizarStatusVenda(@PathVariable Integer vendaId, @RequestParam String status) {
        try {
            return ResponseEntity.ok(pedidoService.atualizarStatusVenda(vendaId, status));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }
}