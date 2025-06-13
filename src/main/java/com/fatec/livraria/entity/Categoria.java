package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CATEGORIAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Integer id;

    @Column(name = "cat_nome", nullable = false)
    private String nome;
}

