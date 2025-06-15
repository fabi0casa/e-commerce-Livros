document.addEventListener("DOMContentLoaded", () => {
  const loader = document.getElementById("loader");
  loader.style.display = "flex";

  fetch("/dashboard/dados")
    .then(response => response.json())
    .then(data => {
      // KPIs
      document.getElementById("livros-total").innerText = data.totalLivros;
      document.getElementById("clientes-total").innerText = data.totalClientes;
      document.getElementById("vendas-mes").innerText = formatarReal(data.vendasMes);
      document.getElementById("ticket-medio").innerText = formatarReal(data.ticketMedio);

      // Gráfico: Receita dos Últimos 30 Dias
      const ctxReceita = document.getElementById('graficoReceita');
      new Chart(ctxReceita, {
        type: 'line',
        data: {
          labels: Array.from({ length: 30 }, (_, i) => `Dia ${i + 1}`),
          datasets: [{
            label: 'R$ Receita',
            data: data.receitaUltimos30Dias,
            fill: true,
            backgroundColor: 'rgba(168, 85, 247, 0.2)',
            borderColor: '#a855f7',
            tension: 0.3
          }]
        },
        options: { responsive: true }
      });

      // Gráfico: Pedidos por Status
      const statusLabels = Object.keys(data.pedidosPorStatus);
      const statusValores = Object.values(data.pedidosPorStatus);
      const statusCores = gerarCores(statusLabels.length);

      const ctxStatus = document.getElementById('graficoStatus');
      new Chart(ctxStatus, {
        type: 'bar',
        data: {
          labels: statusLabels,
          datasets: [{
            label: 'Qtd de Pedidos',
            data: statusValores,
            backgroundColor: statusCores
          }]
        },
        options: { responsive: true }
      });

      // Gráfico: Top 10 Livros Mais Vendidos
      const livrosLabels = data.topLivros.map(item => item.label);
      const livrosValores = data.topLivros.map(item => item.valor);

      const ctxTopLivros = document.getElementById('graficoTopLivros');
      new Chart(ctxTopLivros, {
        type: 'bar',
        data: {
          labels: livrosLabels,
          datasets: [{
            label: 'Vendas',
            data: livrosValores,
            backgroundColor: '#00ff88'
          }]
        },
        options: { responsive: true }
      });

      // Gráfico: Categorias Mais Vendidas
      const categoriasLabels = data.categoriasMaisVendidas.map(item => item.label);
      const categoriasValores = data.categoriasMaisVendidas.map(item => item.valor);
      const categoriasCores = gerarCores(categoriasLabels.length);

      const ctxCategorias = document.getElementById('graficoCategorias');
      new Chart(ctxCategorias, {
        type: 'doughnut',
        data: {
          labels: categoriasLabels,
          datasets: [{
            label: 'Categorias',
            data: categoriasValores,
            backgroundColor: categoriasCores
          }]
        },
        options: { responsive: true }
      });

    })
    .catch(error => {
      console.error("Erro ao carregar dados do dashboard:", error);
    })
    .finally(() => {
      loader.style.display = "none";
    });
});

// Função auxiliar para formatar número em R$ brasileiro
function formatarReal(valor) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
}

// Função para gerar cores aleatórias (usada em gráficos com múltiplas categorias/status)
function gerarCores(qtd) {
  const cores = ['#a855f7', '#00ff88', '#8888ff', '#ffaa00', '#ff4444', '#ff8888', '#88ccff', '#66aa55', '#cc66ff', '#ffcc00'];
  return Array.from({ length: qtd }, (_, i) => cores[i % cores.length]);
}
