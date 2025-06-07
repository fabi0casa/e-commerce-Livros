package com.fatec.livraria.controller;

import com.fatec.livraria.dto.request.PedidoCarrinhoRequest;
import com.fatec.livraria.dto.request.PedidoRequest;
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
    public List<Pedido> listarTodos(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return pedidoService.listarTodos();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Pedido> buscarPorCodigoCliente(@PathVariable String codigo, HttpSession session) {
        try {
            Pedido pedido = pedidoService.buscarPorCodigoDoCliente(codigo, session);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/all/codigo/{codigo}")
    public ResponseEntity<Pedido> buscarPorCodigoAdmin(@PathVariable String codigo, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        try {
            Pedido pedido = pedidoService.buscarPorCodigoComoAdmin(codigo, session);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
    

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Integer clienteId, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return pedidoService.listarPorClienteId(clienteId);
    }

    @GetMapping("/me")
    public ResponseEntity<?> listarPedidosDoClienteLogado(HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        if (clienteId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não está logado.");
        }

        List<Pedido> pedidos = pedidoService.listarPorClienteId(clienteId);
        return ResponseEntity.ok(pedidos);
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
            pedidoService.atualizarStatusVendas(request.getVendaIds(), request.getStatus(), session);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }    
}