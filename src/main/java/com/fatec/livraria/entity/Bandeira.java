package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BANDEIRAS")
@Getter
@Setter
public class Bandeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Integer id;

    @Column(name = "ban_nome", nullable = false, length = 45)
    private String nome;
}
