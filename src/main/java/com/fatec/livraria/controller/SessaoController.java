package com.fatec.livraria.controller;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.request.ClienteLoginRequest;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.service.ClienteService;

@RestController
@RequestMapping("/sessao")
public class SessaoController {

    @Autowired private ClienteService clienteService;

    @PostMapping("/logar")
    public ResponseEntity<Map<String, Object>> logarComoCliente(@RequestBody ClienteLoginRequest cliente, HttpSession session) {
        session.setAttribute("clienteId", cliente.getClienteId());

        Cliente clienteEntity = clienteService.buscarPorId(cliente.getClienteId()).orElse(null);
    
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Cliente logado na sessão.");
        resposta.put("isAdmin", clienteEntity != null && clienteEntity.isAdmin());
    
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/cliente-logado")
    public Integer getClienteLogado(HttpSession session) {
        return (Integer) session.getAttribute("clienteId"); // Retorna o ID do cliente logado
    }
    
    @DeleteMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate(); // Invalida a sessão atual
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Cliente deslogado com sucesso.");
        return ResponseEntity.ok(resposta);
    }
}
