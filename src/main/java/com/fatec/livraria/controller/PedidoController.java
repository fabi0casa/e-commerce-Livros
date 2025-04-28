package com.fatec.livraria.controller;

import com.fatec.livraria.dto.PedidoCarrinhoRequest;
import com.fatec.livraria.dto.PedidoRequest;
import com.fatec.livraria.dto.PedidoResponse;
import com.fatec.livraria.dto.StatusRequest;
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

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Pedido> buscarPorCodigo(@PathVariable String codigo, @RequestParam(required = false) Integer clienteId) {
        try {
            Pedido pedido = pedidoService.buscarPorCodigo(codigo, clienteId);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }    

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Integer clienteId) {
        return pedidoService.listarPorClienteId(clienteId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequest request) {
        try {
            Pedido novoPedido = pedidoService.criarPedidoComVendas(request);

            PedidoResponse response = new PedidoResponse(
                novoPedido.getId(),
                novoPedido.getCodigo()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/add-carrinho")
    public ResponseEntity<?> criarPedidoDoCarrinho(@RequestBody PedidoCarrinhoRequest request) {
        try {
            Pedido novoPedido = pedidoService.criarPedidoDoCarrinho(request);

            PedidoResponse response = new PedidoResponse(
                novoPedido.getId(),
                novoPedido.getCodigo()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }


    @PatchMapping("/vendas/status")
    public ResponseEntity<?> atualizarStatusVendas(@RequestBody StatusRequest request) {
        try {
            pedidoService.atualizarStatusVendas(request.getVendaIds(), request.getStatus());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }    
}