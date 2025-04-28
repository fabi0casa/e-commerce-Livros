package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    // Listar carrinho de um cliente
    @GetMapping("/{clienteId}")
    public ResponseEntity<List<Carrinho>> listarCarrinho(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(carrinhoService.listarCarrinho(clienteId));
    }

    // Adicionar item ao carrinho
    @PostMapping("/adicionar")
    public ResponseEntity<Carrinho> adicionarAoCarrinho(@RequestParam Integer clienteId, 
                                                         @RequestParam Integer livroId, 
                                                         @RequestParam Integer quantidade) {
        return ResponseEntity.ok(carrinhoService.adicionarAoCarrinho(clienteId, livroId, quantidade));
    }

    // Atualizar quantidade de um item
    @PutMapping("/atualizar/{carrinhoId}")
    public ResponseEntity<Carrinho> atualizarQuantidade(@PathVariable Integer carrinhoId, 
                                                        @RequestParam Integer novaQuantidade) {
        return ResponseEntity.ok(carrinhoService.atualizarQuantidade(carrinhoId, novaQuantidade));
    }

    // Remover item do carrinho
    @DeleteMapping("/remover/{carrinhoId}")
    public ResponseEntity<Void> removerDoCarrinho(@PathVariable Integer carrinhoId) {
        carrinhoService.removerDoCarrinho(carrinhoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/carrinho/{clienteId}/clear")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long clienteId) {
        carrinhoService.limparCarrinhoDoCliente(clienteId);
        return ResponseEntity.noContent().build();
    }

}
