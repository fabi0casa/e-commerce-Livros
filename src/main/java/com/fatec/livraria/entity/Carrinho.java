package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "CARRINHO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer id;

    @Column(name = "car_quantidade", nullable = false)
    private Integer quantidade;

    @Temporal(TemporalType.DATE)
    @Column(name = "car_data", nullable = false)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "car_cli_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "car_lvr_id", nullable = false)
    private Livro livro;
}
