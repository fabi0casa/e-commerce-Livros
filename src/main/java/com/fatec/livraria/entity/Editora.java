package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EDITORAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edi_id")
    private Integer id;

    @Column(name = "edi_nome", nullable = false, length = 100)
    private String nome;
}
