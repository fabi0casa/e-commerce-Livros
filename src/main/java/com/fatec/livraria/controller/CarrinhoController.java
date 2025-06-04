package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.service.CarrinhoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    // Listar carrinho do cliente logado
    @GetMapping("/me")
    public ResponseEntity<List<Carrinho>> listarCarrinho(HttpSession session) {
        return carrinhoService.listarCarrinho(session)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // retorna quantidade de itens no carrinho
    @GetMapping("/quantidade")
    public ResponseEntity<Integer> getQuantidadeTotal(HttpSession session) {
        return carrinhoService.calcularQuantidadeTotal(session)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }    

    // Adicionar item ao carrinho
    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarAoCarrinho(HttpSession session,
                                                 @RequestParam Integer livroId,
                                                 @RequestParam Integer quantidade) {
        Optional<Carrinho> carrinhoOptional = carrinhoService.adicionarAoCarrinho(session, livroId, quantidade);
        
        if (carrinhoOptional.isPresent()) {
            return ResponseEntity.ok(carrinhoOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não autenticado");
        }
    }

    // Atualizar quantidade de um item
    @PutMapping("/atualizar/{carrinhoId}")
    public ResponseEntity<Carrinho> atualizarQuantidade(@PathVariable Integer carrinhoId,
                                                        @RequestParam Integer novaQuantidade,
                                                        HttpSession session) {
        return ResponseEntity.ok(carrinhoService.atualizarQuantidade(carrinhoId, novaQuantidade, session));
    }
    
    // Remover item do carrinho
    @DeleteMapping("/remover/{carrinhoId}")
    public ResponseEntity<Void> removerDoCarrinho(@PathVariable Integer carrinhoId, HttpSession session) {
        carrinhoService.removerDoCarrinho(carrinhoId, session);
        return ResponseEntity.noContent().build();
    }    

    // Limpar carrinho do cliente logado
    @DeleteMapping("/limpar")
    public ResponseEntity<Void> limparCarrinho(HttpSession session) {
        if (!carrinhoService.limparCarrinhoDoCliente(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }
}

