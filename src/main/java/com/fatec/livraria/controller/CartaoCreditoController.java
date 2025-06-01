package com.fatec.livraria.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.request.CartaoRequest;
import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.service.BandeiraService;
import com.fatec.livraria.service.CartaoCreditoService;
import com.fatec.livraria.service.ClienteService;

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

    @GetMapping
    public ResponseEntity<List<CartaoCredito>> getAllCartoes() {
        List<CartaoCredito> cartoes = cartaoCreditoService.getAllCartoes();
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CartaoCredito>> getCartaoByCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(cartaoCreditoService.getCartaoByClienteId(clienteId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CartaoCredito> getCartaoById(@PathVariable Integer id) {
        return cartaoCreditoService.getCartaoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> adicionarCartao(@RequestBody CartaoRequest cartaoRequest) {
        try {
            cartaoCreditoService.adicionarCartao(cartaoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Cartão cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao cadastrar cartão."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Integer id) {
        cartaoCreditoService.deletarCartao(id);
        return ResponseEntity.noContent().build();
    }
}

