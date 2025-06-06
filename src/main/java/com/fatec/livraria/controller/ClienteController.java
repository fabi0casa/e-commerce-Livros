package com.fatec.livraria.controller;

import com.fatec.livraria.dto.request.AlterarSenhaRequest;
import com.fatec.livraria.dto.request.AtualizarClienteRequest;
import com.fatec.livraria.dto.request.ClienteRequest;
import com.fatec.livraria.dto.request.EnderecoRequest;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.service.ClienteService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    // Listar todos os clientes
    @GetMapping("/all")
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    } 

    @GetMapping("/me")
    public ResponseEntity<Cliente> buscarClienteLogado(HttpSession session) {
        return clienteService.buscarClienteLogado(session)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    

    // Cadastro (com Thymeleaf)
    @PostMapping("/add")
    public ResponseEntity<?> adicionarCliente(@ModelAttribute ClienteRequest clienteRequest, Model model, RedirectAttributes redirectAttributes) {
        try {
            clienteService.cadastrarNovoCliente(clienteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"mensagem\": \"Cliente cadastrado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao cadastrar cliente.\"}");
        }
    }

    // Atualizar cliente
    @PutMapping("/update")
    public ResponseEntity<?> atualizarCliente(@RequestBody AtualizarClienteRequest clienteRequest, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        try {
            clienteService.atualizarDadosCliente(clienteRequest);
            return ResponseEntity.ok("{\"mensagem\": \"Cliente atualizado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao atualizar cliente.\"}");
        }
    }

    // Atualizar senha
    @PutMapping("/alterarSenha")
    public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaRequest alterarSenhaRequest, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        try {
            clienteService.alterarSenha(alterarSenhaRequest);
            return ResponseEntity.ok("{\"mensagem\": \"Senha alterada com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro ao alterar senha.\"}");
        }
    }

    // Adicionar novo endereço
    @PostMapping("/{clienteId}/enderecos/add")
    public ResponseEntity<?> adicionarEndereco(@PathVariable int clienteId, @RequestBody EnderecoRequest enderecoRequest, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        try {
            clienteService.adicionarEnderecoAoCliente(clienteId, enderecoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"mensagem\": \"Endereço cadastrado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("{\"erro\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao cadastrar endereço.\"}");
        }
    }

    @PostMapping("/me/enderecos/add")
    public ResponseEntity<?> adicionarEnderecoAoClienteLogado(HttpSession session, @RequestBody EnderecoRequest enderecoRequest) {
        try {
            clienteService.adicionarEnderecoAoClienteLogado(enderecoRequest, session);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"mensagem\": \"Endereço cadastrado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("{\"erro\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao cadastrar endereço.\"}");
        }
    }


    // Deletar cliente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }

    // Filtro
    @GetMapping("/filtro")
    public ResponseEntity<List<Cliente>> filtrarClientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataNascimento,
            @RequestParam(required = false) String genero,
            HttpSession session) {

        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        List<Cliente> clientes = clienteService.buscarClientesComFiltro(nome, cpf, telefone, email, dataNascimento, genero);
        return ResponseEntity.ok(clientes);
    }
}

