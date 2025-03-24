package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CUPONS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cup_id")
    private Integer id;

    @Column(name = "cup_codigo", nullable = false, length = 45)
    private String codigo;

    @Column(name = "cup_valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "cup_tipo", nullable = false, length = 45)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "cup_cli_id", nullable = false)
    private Cliente cliente;
}
