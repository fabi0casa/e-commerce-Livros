package com.fatec.livraria.controller;

import com.fatec.livraria.service.DashboardService;
import com.fatec.livraria.dto.response.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard/dados")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardResponse obterDashboard() {
        return dashboardService.getDadosDashboard();
    }
}
