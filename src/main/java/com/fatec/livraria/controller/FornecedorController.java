
package com.fatec.livraria.controller;

import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.service.FornecedorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping("/lista")
    public List<NomeIdResponse> listarTodos() {
        return service.listarTodos();
    }
}
