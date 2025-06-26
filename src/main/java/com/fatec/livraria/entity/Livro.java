package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "LIVROS")
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Audited
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
    @JsonIgnore
    private BigDecimal precoCusto;

    @Column(name = "lvr_preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "lvr_estoque", nullable = false)
    private Integer estoque;

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

    @ManyToMany
    @JoinTable(
        name = "LIVRO_CATEGORIA",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;
 
    //Construtor para a classe LivroInitializer
    public Livro(Integer id, String nome, Integer anoPublicacao, String edicao, String isbn,
    Integer numPaginas, String sinopse, String codigoBarras, String caminhoImagem,
    BigDecimal precoCusto, BigDecimal precoVenda, Autor autor,
    Editora editora, Fornecedor fornecedor, GrupoPrecificacao grupoPrecificacao,
    List<Categoria> categorias, Integer estoque) 
    {
        this.id = id;
        this.nome = nome;
        this.anoPublicacao = anoPublicacao;
        this.edicao = edicao;
        this.isbn = isbn;
        this.numPaginas = numPaginas;
        this.sinopse = sinopse;
        this.codigoBarras = codigoBarras;
        this.caminhoImagem = caminhoImagem;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.autor = autor;
        this.editora = editora;
        this.fornecedor = fornecedor;
        this.grupoPrecificacao = grupoPrecificacao;
        this.categorias = categorias;
        this.estoque = estoque;
    }

}
