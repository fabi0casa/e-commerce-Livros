package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>, JpaSpecificationExecutor<Cliente> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
