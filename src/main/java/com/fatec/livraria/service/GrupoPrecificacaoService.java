package com.fatec.livraria.service;

import com.fatec.livraria.entity.GrupoPrecificacao;
import com.fatec.livraria.repository.GrupoPrecificacaoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GrupoPrecificacaoService {
    private final GrupoPrecificacaoRepository grupoPrecificacaoRepository;

    public GrupoPrecificacaoService(GrupoPrecificacaoRepository grupoPrecificacaoRepository) {
        this.grupoPrecificacaoRepository = grupoPrecificacaoRepository;
    }

    @PostConstruct
    public void inicializarGrupoPrecificacao() {
        if (grupoPrecificacaoRepository.count() == 0) {
            List<GrupoPrecificacao> grupos = List.of(
                new GrupoPrecificacao(null, "Popular", new BigDecimal("10.00")),
                new GrupoPrecificacao(null, "Padrão", new BigDecimal("20.00")),
                new GrupoPrecificacao(null, "Premium", new BigDecimal("30.00")),
                new GrupoPrecificacao(null, "Colecionador", new BigDecimal("40.00"))
            );
            grupoPrecificacaoRepository.saveAll(grupos);
            System.out.println("✅ Grupos de precificação cadastrados automaticamente.");
        }
    }
}
