package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NOTIFICACOES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Notificacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private Integer id;

    @Column(name = "not_titulo", nullable = false)
    private String titulo;

    @Column(name = "not_descricao", nullable = false)
    private String descricao;

    @Column(name = "not_visto", nullable = false)
    private boolean isVisto = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "not_cli_id", nullable = false)
    private Cliente cliente;
}

