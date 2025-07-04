package com.fatec.livraria.controller;

import com.fatec.livraria.dto.request.PedidoCarrinhoRequest;
import com.fatec.livraria.dto.request.PedidoRequest;
import com.fatec.livraria.dto.response.PedidoCompletoResponse;
import com.fatec.livraria.dto.response.PedidoResponse;
import com.fatec.livraria.dto.request.StatusRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.service.PedidoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fatec.livraria.service.PermissaoUsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping("/all")
    public ResponseEntity<List<PedidoCompletoResponse>> listarTodos(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigoCliente(@PathVariable String codigo, HttpSession session) {
        try {
            PedidoCompletoResponse pedido = pedidoService.buscarPorCodigoDoCliente(codigo, session);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/all/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigoAdmin(@PathVariable String codigo, HttpSession session) {
        try {
            permissaoUsuarioService.checarPermissaoDoUsuario(session);
            PedidoCompletoResponse pedido = pedidoService.buscarPorCodigoComoAdmin(codigo, session);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoCompletoResponse>> listarPorCliente(@PathVariable Integer clienteId, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(pedidoService.listarPorClienteId(clienteId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> listarPedidosDoClienteLogado(HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
    
        if (clienteId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não está logado.");
        }
    
        return ResponseEntity.ok(pedidoService.listarPorClienteId(clienteId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoRequest request, HttpSession session) {
        try {
            Pedido novoPedido = pedidoService.criarPedidoComVendas(request, session);

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
    public ResponseEntity<?> criarPedidoDoCarrinho(@Valid @RequestBody PedidoCarrinhoRequest request, HttpSession session) {
        try {
            Pedido novoPedido = pedidoService.criarPedidoDoCarrinho(request, session);

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
    public ResponseEntity<?> atualizarStatusVendas(@RequestBody StatusRequest request, HttpSession session) {
        try {
            pedidoService.atualizarStatusVendas(
                request.getVendaIds(),
                request.getStatus(),
                request.getRetornarEstoque(),
                session
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }    
}