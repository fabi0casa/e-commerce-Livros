package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AUTORES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aut_id")
    private Integer id;

    @Column(name = "aut_nome", nullable = false, length = 100)
    private String nome;
}

