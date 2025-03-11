package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Bandeira;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Integer> {
    List<Bandeira> findByCartoes_Id(Integer cartaoId);
}
