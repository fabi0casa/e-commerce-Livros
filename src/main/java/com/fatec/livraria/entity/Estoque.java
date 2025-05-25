package com.fatec.livraria.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ESTOQUE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "est_id")
    private Integer id;

    @Column(name = "est_quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "est_data_entrada", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;

    @ManyToOne
    @JoinColumn(name = "est_lvr_id", nullable = false)
    @JsonIgnore
    private Livro livro;
}

