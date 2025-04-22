package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CupomRepository extends JpaRepository<Cupom, Integer> {
    boolean existsByCodigo(String codigo);
}
