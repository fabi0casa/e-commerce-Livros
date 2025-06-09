package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Log;
import com.fatec.livraria.service.LogService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired private LogService logService;
    @Autowired private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping("/all")
    public List<Log> listarLogs(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return logService.listarTodos();
    }
}
