package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>, JpaSpecificationExecutor<Cliente> {
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmail(String email);
    
}
