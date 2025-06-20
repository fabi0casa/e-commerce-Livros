package com.fatec.livraria.entity;

import java.util.List;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BANDEIRAS")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class Bandeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Integer id;

    @Column(name = "ban_nome", nullable = false, length = 45)
    private String nome;

    @OneToMany(mappedBy = "bandeira")
    @JsonIgnore
    private List<CartaoCredito> cartoes;

    // Construtor que recebe um nome
    public Bandeira(String nome) {
        this.nome = nome;
    }

}
