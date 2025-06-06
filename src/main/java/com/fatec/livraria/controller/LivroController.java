package com.fatec.livraria.controller;

import com.fatec.livraria.dto.response.LivroEstoqueResponse;
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
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Integer id) {
        Optional<Livro> livro = livroService.buscarPorId(id);
        return livro.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Livro>> buscarPorNome(@PathVariable String nome) {
        List<Livro> livros = livroService.buscarPorNome(nome);
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

}
