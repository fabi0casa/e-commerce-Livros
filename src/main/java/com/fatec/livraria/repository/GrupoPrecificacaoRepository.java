package com.fatec.livraria.repository;

import com.fatec.livraria.entity.GrupoPrecificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoPrecificacaoRepository extends JpaRepository<GrupoPrecificacao, Integer> {
}
