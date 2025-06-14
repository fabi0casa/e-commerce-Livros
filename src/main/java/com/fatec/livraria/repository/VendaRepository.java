package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Venda;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendaRepository extends JpaRepository<Venda, Integer> {

    @Query("""
        SELECT COUNT(v) FROM Venda v
        WHERE v.livro.id = :livroId
        AND v.pedido.dataCriacao BETWEEN :inicio AND :fim
    """)
    Long countVendasLivroNoPeriodo(@Param("livroId") Integer livroId,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim);

    @Query("""
        SELECT COUNT(v) FROM Venda v
        JOIN v.livro l
        JOIN l.categorias c
        WHERE c.id = :categoriaId
        AND v.pedido.dataCriacao BETWEEN :inicio AND :fim
    """)
    Long countVendasCategoriaNoPeriodo(
        @Param("categoriaId") Integer categoriaId,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );

}
