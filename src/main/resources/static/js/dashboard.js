 // Exemplo de dados fictícios para prototipar os gráficos
 document.getElementById("livros-total").innerText = 1820;
 document.getElementById("clientes-total").innerText = 532;
 document.getElementById("vendas-mes").innerText = "R$ 23.440,50";
 document.getElementById("ticket-medio").innerText = "R$ 44,10";

 const ctxReceita = document.getElementById('graficoReceita');
 const receitaChart = new Chart(ctxReceita, {
   type: 'line',
   data: {
     labels: [...Array(30).keys()].map(i => `Dia ${i + 1}`),
     datasets: [{
       label: 'R$ Receita',
       data: [...Array(30)].map(() => Math.floor(Math.random() * 800)),
       fill: true,
       backgroundColor: 'rgba(168, 85, 247, 0.2)',
       borderColor: '#a855f7',
       tension: 0.3
     }]
   },
   options: { responsive: true }
 });

 const ctxStatus = document.getElementById('graficoStatus');
 new Chart(ctxStatus, {
   type: 'bar',
   data: {
     labels: ['Processando', 'Aprovado', 'Transporte', 'Entregue', 'Cancelado'],
     datasets: [{
       label: 'Qtd de Pedidos',
       data: [12, 24, 10, 35, 3],
       backgroundColor: ['#a855f7', '#00ff88', '#8888ff', '#ffaa00', '#ff4444']
     }]
   },
   options: { responsive: true }
 });

 const ctxTopLivros = document.getElementById('graficoTopLivros');
 new Chart(ctxTopLivros, {
   type: 'bar',
   data: {
     labels: ['Livro A', 'Livro B', 'Livro C', 'Livro D', 'Livro E', 'Livro F', 'Livro G', 'Livro H', 'Livro I', 'Livro J'],
     datasets: [{
       label: 'Vendas',
       data: [120, 110, 95, 90, 85, 80, 75, 70, 68, 65],
       backgroundColor: '#00ff88'
     }]
   },
   options: { responsive: true }
 });

 const ctxCategorias = document.getElementById('graficoCategorias');
 new Chart(ctxCategorias, {
   type: 'doughnut',
   data: {
     labels: ['Romance', 'Ficção', 'Autoajuda', 'Tecnologia', 'História'],
     datasets: [{
       label: 'Categorias',
       data: [45, 30, 25, 20, 15],
       backgroundColor: ['#a855f7', '#00ff88', '#ff8888', '#88ccff', '#ffaa00']
     }]
   },
   options: { responsive: true }
 });