package com.fatec.livraria.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.livraria.dto.request.CartaoClienteLogadoRequest;
import com.fatec.livraria.dto.request.CartaoRequest;
import com.fatec.livraria.dto.request.CartaoUpdateRequest;
import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.service.BandeiraService;
import com.fatec.livraria.service.CartaoCreditoService;
import com.fatec.livraria.service.ClienteService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cartoes")
public class CartaoCreditoController {

    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BandeiraService bandeiraService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping
    public ResponseEntity<List<CartaoCredito>> getAllCartoes(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        List<CartaoCredito> cartoes = cartaoCreditoService.getAllCartoes();
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CartaoCredito>> getCartaoByCliente(@PathVariable Integer clienteId, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(cartaoCreditoService.getCartaoByClienteId(clienteId));
    }

    @GetMapping("/cliente/me")
    public ResponseEntity<List<CartaoCredito>> getCartoesDoClienteLogado(HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        List<CartaoCredito> cartoes = cartaoCreditoService.getCartaoByClienteId(cliente.getId());
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoCredito> getCartaoById(@PathVariable Integer id, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return cartaoCreditoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{cartaoId}/me")
    public ResponseEntity<?> buscarCartaoDoClienteLogado(@PathVariable Integer cartaoId, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        CartaoCredito cartao = cartaoCreditoService.buscarCartaoDoClienteLogado(cartaoId, clienteId);
        return ResponseEntity.ok(cartao);
    }

    @PostMapping("/add")
    public ResponseEntity<?> adicionarCartao(@RequestBody CartaoRequest cartaoRequest, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        try {
            cartaoCreditoService.adicionarCartao(cartaoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Cartão cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao cadastrar cartão."));
        }
    }

    @PostMapping("/me/add")
    public ResponseEntity<?> adicionarCartaoAoClienteLogado(@RequestBody CartaoClienteLogadoRequest cartaoClienteLogadoRequest, HttpSession session) {
        try {
            cartaoCreditoService.adicionarCartaoAoClienteLogado(cartaoClienteLogadoRequest, session);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Cartão cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao cadastrar cartão."));
        }
    }

    @PutMapping("/{cartaoId}/update")
    public ResponseEntity<?> atualizarCartao(
            @PathVariable Integer cartaoId,
            @RequestBody CartaoUpdateRequest request,
            HttpSession session) {

        permissaoUsuarioService.checarPermissaoDoUsuario(session);

        try {
            cartaoCreditoService.atualizarCartao(cartaoId, request, session);
            return ResponseEntity.ok(Map.of("mensagem", "Cartão atualizado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro inesperado ao atualizar o cartão."));
        }
    }

    @PutMapping("/{cartaoId}/me/update")
    public ResponseEntity<?> atualizarCartaoDoClienteLogado(
            @PathVariable Integer cartaoId,
            @RequestBody CartaoUpdateRequest request,
            HttpSession session) {
        try {
            cartaoCreditoService.atualizarCartaoDoClienteLogado(cartaoId, request, session);
            return ResponseEntity.ok(Map.of("mensagem", "Cartão atualizado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao atualizar cartão."));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Integer id, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        cartaoCreditoService.deletarCartao(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/me/{cartaoId}")
    public ResponseEntity<?> deletarCartaoDoClienteLogado(@PathVariable Integer cartaoId, HttpSession session) {
        try {
            cartaoCreditoService.deletarCartaoDoClienteLogado(cartaoId, session);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao deletar cartão."));
        }
    }

}

