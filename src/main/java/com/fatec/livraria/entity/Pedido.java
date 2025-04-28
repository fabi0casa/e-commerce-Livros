package com.fatec.livraria.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PEDIDOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ped_id")
    private Integer id;

    @Column(name = "ped_codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "ped_valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "ped_forma_pagamento", nullable = false, length = 45)
    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "ped_end_id", nullable = false)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "ped_cli_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venda> vendas;

}
