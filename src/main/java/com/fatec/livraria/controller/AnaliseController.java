package com.fatec.livraria.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.response.AnaliseResponse;
import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.service.AnaliseService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/analise")
public class AnaliseController {

    @Autowired
    private AnaliseService analiseService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping("/livros")
    public ResponseEntity<List<NomeIdResponse>> listarLivros(
        @RequestParam(required = false) String nome,
        HttpSession session
    ) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        List<NomeIdResponse> response = analiseService.analisarLivros(nome);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<NomeIdResponse>> listarCategorias(
        @RequestParam(required = false) String nome,
        HttpSession session
    ) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        List<NomeIdResponse> response = analiseService.analisarCategorias(nome);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/livros/por-data")
    public ResponseEntity<List<AnaliseResponse>> vendasPorLivroAoLongoDoTempo(
        @RequestParam List<Integer> ids,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
        HttpSession session
    ) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(analiseService.analisarVendasLivroPorData(ids, dataInicio, dataFim));
    }

    @GetMapping("/categorias/por-data")
    public ResponseEntity<List<AnaliseResponse>> vendasPorCategoriaAoLongoDoTempo(
        @RequestParam List<Integer> ids,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
        HttpSession session
    ) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(analiseService.analisarVendasCategoriaPorData(ids, dataInicio, dataFim));
    }

}
