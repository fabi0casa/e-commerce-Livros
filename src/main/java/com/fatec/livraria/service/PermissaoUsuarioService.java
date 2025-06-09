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
        Object clienteIdObj = session.getAttribute("clienteId");
    
        if (clienteIdObj == null) {
            throw new AcessoNegadoException();
        }
    
        Integer clienteId = (Integer) clienteIdObj;
    
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(AcessoNegadoException::new);
    
        if (!cliente.isAdmin()) {
            throw new AcessoNegadoException();
        }
    }    
}
