package com.fatec.livraria.entity;

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

    @ManyToOne
    @JoinColumn(name = "ped_end_id", nullable = false)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "ped_cli_id", nullable = false)
    private Cliente cliente;
}
