package com.fatec.livraria.service;

import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public Venda atualizarStatus(Integer vendaId, String novoStatus) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
        venda.setStatus(novoStatus);
        return vendaRepository.save(venda);
    }
}
