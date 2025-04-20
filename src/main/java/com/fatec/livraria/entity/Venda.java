package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "VENDAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vnd_id")
    private Integer id;

    //tirar
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vnd_data_hora", nullable = false)
    private Date dataHora;

    //tirar
    @Column(name = "vnd_forma_pagamento", nullable = false, length = 45)
    private String formaPagamento;

    @Column(name = "vnd_status", nullable = false, length = 35)
    private String status;

    @Column(name = "vnd_valor", nullable = false)
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "vnd_lvr_id", nullable = false)
    private Livro livro;

    //tirar
    @ManyToOne
    @JoinColumn(name = "vnd_end_id", nullable = false)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "vnd_ped_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;
}
