package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Bandeira;
import com.fatec.livraria.service.BandeiraService;
import com.fatec.livraria.service.PermissaoUsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bandeiras")
public class BandeiraController {

    @Autowired
    private BandeiraService bandeiraService;

    @Autowired
    private PermissaoUsuarioService permissaoUsuarioService;

    @GetMapping("/all")
    public ResponseEntity<List<Bandeira>> getAllBandeiras() {
        return ResponseEntity.ok(bandeiraService.getAllBandeiras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bandeira> getBandeiraById(@PathVariable Integer id) {
        return bandeiraService.getBandeiraById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cartao/{cartaoId}")
    public ResponseEntity<List<Bandeira>> getBandeirasByCartao(@PathVariable Integer cartaoId, HttpSession session) {
        permissaoUsuarioService.checarPermissaoDoUsuario(session);
        return ResponseEntity.ok(bandeiraService.getBandeirasByCartaoId(cartaoId));
    }

    
}
