package com.fatec.livraria.service;

import com.fatec.livraria.dto.response.DashboardResponse;
import com.fatec.livraria.dto.response.StatusQuantidadeResponse;
import com.fatec.livraria.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public DashboardResponse getDadosDashboard() {
        DashboardResponse response = new DashboardResponse();

        response.setTotalLivros((int) livroRepository.count());
        response.setTotalClientes((int) clienteRepository.count());

        // Vendas no mês
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDiaMes = hoje.withDayOfMonth(1);
        BigDecimal totalVendasMes = pedidoRepository.findTotalVendasPeriodo(primeiroDiaMes, hoje);
        response.setVendasMes(totalVendasMes);

        // Ticket médio
        long totalPedidosMes = pedidoRepository.countByDataCriacaoBetween(primeiroDiaMes, hoje);
        BigDecimal ticketMedio = totalPedidosMes > 0 ? totalVendasMes.divide(BigDecimal.valueOf(totalPedidosMes), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        response.setTicketMedio(ticketMedio);

        // Receita últimos 30 dias
        LocalDate inicio = hoje.minusDays(29);
        List<BigDecimal> receitaPorDia = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LocalDate dia = inicio.plusDays(i);
            BigDecimal totalDia = pedidoRepository.findTotalVendasPeriodo(dia, dia);
            receitaPorDia.add(totalDia);
        }
        response.setReceitaUltimos30Dias(receitaPorDia);

        // Pedidos por status
        Map<String, Long> pedidosPorStatus = vendaRepository.countPedidosPorStatus().stream()
        .collect(Collectors.toMap(StatusQuantidadeResponse::getStatus, StatusQuantidadeResponse::getTotal));    
        response.setPedidosPorStatus(pedidosPorStatus);


        // Top 10 livros mais vendidos
        response.setTopLivros(
            vendaRepository.findTop10LivrosMaisVendidos(PageRequest.of(0, 10))
        );

        // Categorias mais vendidas
        response.setCategoriasMaisVendidas(vendaRepository.findCategoriasMaisVendidas());

        return response;
    }
}
