package com.fatec.livraria.controller;

import com.fatec.livraria.dto.request.EnderecoRequest;
import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.service.EnderecoService;
import com.fatec.livraria.service.EnderecoValidator;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoValidator enderecoValidator;

    @GetMapping("/all")
    public ResponseEntity<List<Endereco>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Integer id) {
        Optional<Endereco> endereco = enderecoService.buscarPorId(id);
        return endereco.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Endereco>> getEnderecoByCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(enderecoService.getEnderecoByClienteId(clienteId));
    }

    @PostMapping("/add")
    public ResponseEntity<Endereco> salvar(@RequestBody Endereco endereco) {
        endereco.gerarFraseIdentificadora(); // Gera a frase antes de salvar
        Endereco novoEndereco = enderecoService.salvar(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @PutMapping("/{enderecoId}/update")
    public ResponseEntity<?> atualizarEndereco(
            @PathVariable int enderecoId,
            @RequestBody EnderecoRequest enderecoRequest) {
        try {
            enderecoService.atualizarEndereco(enderecoId, enderecoRequest);
            return ResponseEntity.ok(Map.of("mensagem", "Endereço atualizado com sucesso!"));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro inesperado ao atualizar endereço."));
        }
    }    

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
