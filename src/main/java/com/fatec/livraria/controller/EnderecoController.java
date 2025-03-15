package com.fatec.livraria.controller;

import com.fatec.livraria.dto.EnderecoDTO;
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

    //atualizar o endereço de um cliente
    @PutMapping("/{enderecoId}/update")
    public ResponseEntity<?> atualizarEndereco(
            @PathVariable int enderecoId,
            @RequestBody EnderecoDTO enderecoDTO) {
        try {

            // Buscar o endereço pelo ID e garantir que pertence ao cliente
            Endereco endereco = enderecoService.buscarPorId(enderecoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

            // Validar os dados do endereço
            enderecoValidator.validarEndereco(enderecoDTO);

            // Atualizar os dados do endereço
            endereco.setTipo(enderecoDTO.getTipo());
            endereco.setLogradouro(enderecoDTO.getLogradouro());
            endereco.setNumero(enderecoDTO.getNumero());
            endereco.setBairro(enderecoDTO.getBairro());
            endereco.setCep(enderecoDTO.getCep());
            endereco.setCidade(enderecoDTO.getCidade());
            endereco.setEstado(enderecoDTO.getEstado());
            endereco.setPais(enderecoDTO.getPais());
            endereco.setObservacoes(enderecoDTO.getObservacoes());
            endereco.setResidencial(enderecoDTO.getResidencial());
            endereco.setEntrega(enderecoDTO.getEntrega());
            endereco.setCobranca(enderecoDTO.getCobranca());
            endereco.gerarFraseIdentificadora();

            // Salvar endereço atualizado
            enderecoService.atualizarEndereco(endereco);

            return ResponseEntity.ok("{\"mensagem\": \"Endereço atualizado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("{\"erro\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao atualizar endereço.\"}");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco endereco) {
        try {
            Endereco enderecoAtualizado = enderecoService.atualizarEndereco(endereco);
            return ResponseEntity.ok(enderecoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
