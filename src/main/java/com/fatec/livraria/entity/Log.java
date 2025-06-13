package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LOGS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Log { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "log_data_hora", nullable = false)
    private String dataHora;

    @Column(name = "log_usuario", nullable = false)
    private String usuario;

    @Column(name = "log_descricao", nullable = false)
    private String descricao;
}

