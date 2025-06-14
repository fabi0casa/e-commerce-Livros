package com.fatec.livraria.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PEDIDOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
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

    @Column(name = "ped_data_criacao", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataCriacao = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "ped_end_id", nullable = true)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "ped_cli_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venda> vendas;    

}
