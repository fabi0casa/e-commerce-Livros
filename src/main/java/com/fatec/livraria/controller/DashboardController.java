package com.fatec.livraria.controller;

import com.fatec.livraria.service.DashboardService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

import com.fatec.livraria.dto.response.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard/dados")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping
    public DashboardResponse obterDashboard(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return dashboardService.getDadosDashboard();
    }
}
