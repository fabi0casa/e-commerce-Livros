package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Cupom;
import com.fatec.livraria.repository.CupomRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CupomService {

    @Autowired private CupomRepository cupomRepository;
    @Autowired private NotificacaoService notificacaoService;

    @Transactional
    public void excluirCupom(Integer id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    
        Cliente cliente = cupom.getCliente();
        if (cliente != null && cliente.getCupons() != null) {
            cliente.getCupons().remove(cupom);
        }
    
        cupomRepository.delete(cupom);
    }
    

    @Transactional
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

        Cupom cupomSalvo = cupomRepository.save(cupom);

        notificacaoService.criarNotificacao(
            "🎫 Cupom",
            "Um cupom de " + cupom.getTipo() + "Foi adicionado a sua conta com o valor de R$" + cupom.getValor() + "!",
            cliente.getId()
        );

        return cupomSalvo;
    }

    public List<Cupom> buscarPorIds(List<Integer> ids) {
        return cupomRepository.findAllById(ids);
    }    
    
}
