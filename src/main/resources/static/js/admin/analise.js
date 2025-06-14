document.addEventListener("DOMContentLoaded", () => {
    carregarCheckboxes('/analise/livros', 'lista-livros');
    carregarCheckboxes('/analise/categorias', 'lista-categorias');
});

async function carregarCheckboxes(url, containerId) {
    const container = document.getElementById(containerId);
    const inputPesquisa = container.querySelector('.filtro-input');

    const response = await fetch(url);
    const dados = await response.json();

    dados.forEach(item => {
        const label = document.createElement('label');
        label.innerHTML = `<input type="checkbox" value="${item.id}" data-nome="${item.nome}"> ${item.nome}`;
        container.appendChild(label);
    });
}

function filtrarCheckboxes(input, containerId) {
    const filtro = input.value.toLowerCase();
    const container = document.getElementById(containerId);
    const labels = container.querySelectorAll('label');

    labels.forEach(label => {
        const texto = label.textContent.toLowerCase();
        label.style.display = texto.includes(filtro) ? '' : 'none';
    });
}

async function gerarGrafico() {
    const ctx = document.getElementById('graficoVendas').getContext('2d');

    const livrosSelecionados = Array.from(document.querySelectorAll('#lista-livros input[type="checkbox"]:checked')).map(cb => ({
        id: cb.value,
        nome: cb.dataset.nome
    }));

    const categoriasSelecionadas = Array.from(document.querySelectorAll('#lista-categorias input[type="checkbox"]:checked')).map(cb => ({
        id: cb.value,
        nome: cb.dataset.nome
    }));

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    const parametros = new URLSearchParams();
    if (startDate) parametros.append("dataInicio", startDate);
    if (endDate) parametros.append("dataFim", endDate);

    const dadosLivros = await fetch('/analise/livros?' + parametros.toString()).then(res => res.json());
    const dadosCategorias = await fetch('/analise/categorias?' + parametros.toString()).then(res => res.json());

    const datasets = [];

    livrosSelecionados.forEach(livro => {
        const item = dadosLivros.find(l => l.id == livro.id);
        if (item) {
            datasets.push({
                label: `Livro: ${livro.nome}`,
                data: [item.totalVendas],
                borderColor: gerarCor(),
                fill: false
            });
        }
    });

    categoriasSelecionadas.forEach(categoria => {
        const item = dadosCategorias.find(c => c.id == categoria.id);
        if (item) {
            datasets.push({
                label: `Categoria: ${categoria.nome}`,
                data: [item.totalVendas],
                borderColor: gerarCor(),
                borderDash: [5, 5],
                fill: false
            });
        }
    });

    if (window.graficoVendasInstance) {
        window.graficoVendasInstance.destroy();
    }

    window.graficoVendasInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Total de Vendas'], // aqui você pode incluir datas específicas no futuro
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Análise de Vendas'
                }
            }
        }
    });
}

function gerarCor() {
    const r = Math.floor(Math.random() * 156 + 100);
    const g = Math.floor(Math.random() * 156 + 100);
    const b = Math.floor(Math.random() * 156 + 100);
    return `rgb(${r}, ${g}, ${b})`;
}
