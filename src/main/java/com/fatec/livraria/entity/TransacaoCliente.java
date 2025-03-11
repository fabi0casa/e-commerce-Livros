package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACOES CLIENTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tra_id")
    private Integer id;

    @Column(name = "tra_data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "tra_tipo_operacao", nullable = false, length = 45)
    private String tipoOperacao;

    @Column(name = "tra_quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "tra_valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "tra_forma_pagamento", nullable = false, length = 45)
    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "tra_cli_id", referencedColumnName = "cli_id", nullable = false)
    private Cliente cliente;
}
