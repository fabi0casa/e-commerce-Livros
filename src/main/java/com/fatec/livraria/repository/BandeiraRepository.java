package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Bandeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Integer> {
}
