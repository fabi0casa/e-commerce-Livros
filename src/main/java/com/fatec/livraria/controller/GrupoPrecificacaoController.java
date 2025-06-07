package com.fatec.livraria.controller;

import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.service.GrupoPrecificacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grupo-precificacao")
public class GrupoPrecificacaoController {

    private final GrupoPrecificacaoService service;

    public GrupoPrecificacaoController(GrupoPrecificacaoService service) {
        this.service = service;
    }

    @GetMapping("/lista")
    public List<NomeIdResponse> listarTodos() {
        return service.listarTodos();
    }
}
