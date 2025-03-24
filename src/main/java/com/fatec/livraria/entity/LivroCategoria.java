package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LIVROS_CATEGORIAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lvc_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lvc_lvr_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "lvc_cat_id", nullable = false)
    private Categoria categoria;
}

