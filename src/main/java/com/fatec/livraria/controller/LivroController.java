package com.fatec.livraria.controller;

import com.fatec.livraria.dto.request.EntradaEstoqueRequest;
import com.fatec.livraria.dto.response.LivroDetalhadoResponse;
import com.fatec.livraria.dto.response.LivroEstoqueResponse;
import com.fatec.livraria.dto.response.LivroResumoResponse;
import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.service.LivroService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    public LivroController(LivroService livroService, PermissaoUsuarioService permissaoUsuarioService) {
        this.livroService = livroService;
        this.permissaoUsuarioService = permissaoUsuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<LivroResumoResponse>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodosResumido());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Integer id) {
        Optional<Livro> livro = livroService.buscarPorId(id);
        return livro.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<LivroDetalhadoResponse>> buscarPorNome(@PathVariable String nome) {
        List<LivroDetalhadoResponse> livros = livroService.buscarPorNome(nome);
        if (livros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(livros);
    }    

    @GetMapping("/estoque")
    public ResponseEntity<List<LivroEstoqueResponse>> listarLivrosParaEstoque(HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        List<LivroEstoqueResponse> livrosEstoque = livroService.listarLivrosParaEstoque();
        return ResponseEntity.ok(livrosEstoque);
    }

    @PatchMapping("/estoque/entrada")
    public ResponseEntity<Void> atualizarEstoque(@RequestBody EntradaEstoqueRequest dto, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        boolean atualizado = livroService.atualizarEstoque(dto);
        if (atualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lista")
    public List<NomeIdResponse> listarNomeEId() {
        return livroService.listarNomeEId();
    }

    @GetMapping("/buscar-nome/{id}")
    public ResponseEntity<NomeIdResponse> buscarNomePorId(@PathVariable Integer id) {
        return livroService.buscarNomePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
