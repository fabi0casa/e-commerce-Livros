package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FORNECEDORES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "for_id")
    private Integer id;

    @Column(name = "for_nome", nullable = false, length = 100)
    private String nome;
}
