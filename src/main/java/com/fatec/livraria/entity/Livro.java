package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "LIVROS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lvr_id")
    private Integer id;

    @Column(name = "lvr_nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "lvr_ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @Column(name = "lvr_edicao", nullable = false, length = 45)
    private String edicao;

    @Column(name = "lvr_isbn", nullable = false, length = 45)
    private String isbn;

    @Column(name = "lvr_num_paginas", nullable = false)
    private Integer numPaginas;

    @Column(name = "lvr_sinopse", nullable = false, length = 255)
    private String sinopse;

    @Column(name = "lvr_codigo_barras", nullable = false, length = 60)
    private String codigoBarras;

    @Column(name = "lvr_caminho_imagem", nullable = false, length = 255)
    private String caminhoImagem;

    @Column(name = "lvr_preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "lvr_preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @ManyToOne
    @JoinColumn(name = "lvr_aut_id", nullable = false)
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "lvr_edi_id", nullable = false)
    private Editora editora;

    @ManyToOne
    @JoinColumn(name = "lvr_for_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "lvr_gpr_id", nullable = false)
    private GrupoPrecificacao grupoPrecificacao;
}
