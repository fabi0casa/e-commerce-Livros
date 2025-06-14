package com.fatec.livraria.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.response.AnaliseResponse;

import com.fatec.livraria.service.AnaliseService;

@RestController
@RequestMapping("/analise")
public class AnaliseController {

    @Autowired
    private AnaliseService analiseService;

    @GetMapping("/livros")
    public ResponseEntity<List<AnaliseResponse>> listarLivros(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        List<AnaliseResponse> response = analiseService.analisarLivros(nome, dataInicio, dataFim);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<AnaliseResponse>> listarCategorias(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        List<AnaliseResponse> response = analiseService.analisarCategorias(nome, dataInicio, dataFim);
        return ResponseEntity.ok(response);
    }
}
