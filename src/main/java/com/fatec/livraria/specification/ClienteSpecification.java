package com.fatec.livraria.specification;

import com.fatec.livraria.entity.Cliente;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteSpecification {
    public static Specification<Cliente> filtrarClientes(String nome, String cpf, String telefone, String email, LocalDate dataNascimento, String genero) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (cpf != null && !cpf.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), cpf));
            }
            if (telefone != null && !telefone.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("telefone"), telefone));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("email"), email));
            }
            if (dataNascimento != null) {
                predicates.add(criteriaBuilder.equal(root.get("dataNascimento"), dataNascimento));
            }
            if (genero != null && !genero.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("genero"), genero));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
