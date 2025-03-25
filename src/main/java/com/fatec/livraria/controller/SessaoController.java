package com.fatec.livraria.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.ClienteLoginDTO;

@RestController
@RequestMapping("/sessao")
public class SessaoController {
    @PostMapping("/logar")
    public String logarComoCliente(@RequestBody ClienteLoginDTO cliente, HttpSession session) {
        session.setAttribute("clienteId", cliente.getClienteId());
        return "Cliente logado na sessão.";
    }

    @GetMapping("/cliente-logado")
    public Integer getClienteLogado(HttpSession session) {
        return (Integer) session.getAttribute("clienteId"); // Retorna o ID do cliente logado
    }
}
