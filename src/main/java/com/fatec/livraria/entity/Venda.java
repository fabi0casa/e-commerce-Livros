package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vnd_data_hora", nullable = false)
    private Date dataHora;

    @Column(name = "vnd_quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "vnd_forma_pagamento", nullable = false, length = 45)
    private String formaPagamento;

    @Column(name = "vnd_status", nullable = false, length = 15)
    private String status;

    @ManyToOne
    @JoinColumn(name = "vnd_cli_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vnd_lvr_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "vnd_end_id", nullable = false)
    private Endereco endereco;
}
