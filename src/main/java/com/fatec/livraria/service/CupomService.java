package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Cupom;
import com.fatec.livraria.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    public Cupom gerarCupom(BigDecimal valor, String tipo, Cliente cliente) {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (cupomRepository.existsByCodigo(codigo)); // Verifica se já existe
    
        Cupom cupom = new Cupom();
        cupom.setValor(valor);
        cupom.setTipo(tipo);
        cupom.setCliente(cliente);
        cupom.setCodigo(codigo);
    
        return cupomRepository.save(cupom);
    }
    
}
