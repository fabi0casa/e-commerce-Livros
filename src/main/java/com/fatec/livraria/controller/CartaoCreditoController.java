package com.fatec.livraria.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.service.CartaoCreditoService;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import java.util.List;
//import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoCreditoController {

    @Autowired
    private CartaoCreditoService cartaoCreditoService;

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

    @PostMapping
    public ResponseEntity<CartaoCredito> criarCartao(@Valid @RequestBody CartaoCredito cartaoCredito) {
        CartaoCredito novoCartao = cartaoCreditoService.salvarCartao(cartaoCredito);
        return ResponseEntity.ok(novoCartao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Integer id) {
        cartaoCreditoService.deletarCartao(id);
        return ResponseEntity.noContent().build();
    }
}

