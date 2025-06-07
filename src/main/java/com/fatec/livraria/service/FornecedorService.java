package com.fatec.livraria.service;

import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.entity.Fornecedor;
import com.fatec.livraria.repository.FornecedorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @PostConstruct
    public void inicializarFornecedores() {
        if (fornecedorRepository.count() == 0) {
            List<Fornecedor> fornecedores = List.of(
                new Fornecedor(null, "Distribuidora Suzano"),
                new Fornecedor(null, "Pernambucanas"),
                new Fornecedor(null, "Editora Independente"),
                new Fornecedor(null, "Fornecedor Nacional")
            );
            fornecedorRepository.saveAll(fornecedores);
            System.out.println("✅ Fornecedores cadastrados automaticamente.");
        }
    }

    public List<NomeIdResponse> listarTodos() {
        return fornecedorRepository.findAll()
            .stream()
            .map(f -> new NomeIdResponse(f.getId(), f.getNome()))
            .toList();
    }

}
