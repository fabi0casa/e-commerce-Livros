package com.fatec.livraria.repository;

import com.fatec.livraria.dto.response.DashboardResponse;
import com.fatec.livraria.dto.response.LabelValorResponse;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.dto.response.StatusQuantidadeResponse;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new com.fatec.livraria.dto.response.StatusQuantidadeResponse(v.status, COUNT(v)) " +
    "FROM Venda v GROUP BY v.status")
    List<StatusQuantidadeResponse> countPedidosPorStatus();


    @Query("SELECT new com.fatec.livraria.dto.response.LabelValorResponse(v.livro.nome, COUNT(v)) " +
        "FROM Venda v GROUP BY v.livro.nome ORDER BY COUNT(v) DESC")
    List<LabelValorResponse> findTop10LivrosMaisVendidos(Pageable pageable);


    @Query("SELECT new com.fatec.livraria.dto.response.LabelValorResponse(c.nome, COUNT(v)) " +
        "FROM Venda v JOIN v.livro l JOIN l.categorias c " +
        "GROUP BY c.nome ORDER BY COUNT(v) DESC")
    List<LabelValorResponse> findCategoriasMaisVendidas();


}
