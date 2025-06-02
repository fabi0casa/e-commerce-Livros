package com.fatec.livraria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.livraria.configuration.AcessoNegadoException;
import com.fatec.livraria.entity.Cliente;

import jakarta.servlet.http.HttpSession;

@Service
public class PermissaoUsuarioService {

    @Autowired private ClienteService clienteService;

    public void checarPermissaoDoUsuario(HttpSession session) {
        Cliente cliente = clienteService.buscarPorId((Integer) session.getAttribute("clienteId"))
            .orElseThrow(() -> new AcessoNegadoException());

        if (cliente == null || !cliente.isAdmin()) {
            throw new AcessoNegadoException();
        }

        return;
    }
}
