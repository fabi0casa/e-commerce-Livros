package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "GRUPO_PRECIFICACAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class GrupoPrecificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpr_id")
    private Integer id;

    @Column(name = "gpr_nome", nullable = false, length = 45)
    private String nome;

    @Column(name = "gpr_margem_lucro", nullable = false, precision = 5, scale = 2)
    private BigDecimal margemLucro;
}
