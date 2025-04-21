package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "vnd_status", nullable = false, length = 35)
    private String status;

    @Column(name = "vnd_valor", nullable = false)
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "vnd_lvr_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "vnd_ped_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;
}
