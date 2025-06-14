document.addEventListener("DOMContentLoaded", () => {
    carregarCheckboxes('/analise/livros', 'lista-livros');
    carregarCheckboxes('/analise/categorias', 'lista-categorias');

    // Preenche os campos de data
    const hoje = new Date();
    const trintaDiasAtras = new Date();
    trintaDiasAtras.setDate(hoje.getDate() - 30);

    const formatarData = (data) => {
        const ano = data.getFullYear();
        const mes = String(data.getMonth() + 1).padStart(2, '0');
        const dia = String(data.getDate()).padStart(2, '0');
        return `${ano}-${mes}-${dia}`;
    };

    document.getElementById('endDate').value = formatarData(hoje);
    document.getElementById('startDate').value = formatarData(trintaDiasAtras);
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

    const labels = []; // datas no eixo X
    const datasets = [];

    // === Requisição de livros ao longo do tempo ===
    if (livrosSelecionados.length > 0 && startDate && endDate) {
        const paramsLivros = new URLSearchParams();
        livrosSelecionados.forEach(l => paramsLivros.append("ids", l.id));
        paramsLivros.append("dataInicio", startDate);
        paramsLivros.append("dataFim", endDate);

        const dadosLivros = await fetch('/analise/livros/por-data?' + paramsLivros.toString())
            .then(res => res.json());

        dadosLivros.forEach(livro => {
            // Preencher labels apenas uma vez (assumindo todas as listas têm as mesmas datas)
            if (labels.length === 0 && livro.dados.length > 0) {
                livro.dados.forEach(ponto => labels.push(ponto.label));
            }

            datasets.push({
                label: `Livro: ${livro.nome}`,
                data: livro.dados.map(p => p.total),
                borderColor: gerarCor(),
                fill: false
            });
        });
    }

    // === Requisição de categorias ao longo do tempo ===
    if (categoriasSelecionadas.length > 0 && startDate && endDate) {
        const paramsCategorias = new URLSearchParams();
        categoriasSelecionadas.forEach(c => paramsCategorias.append("ids", c.id));
        paramsCategorias.append("dataInicio", startDate);
        paramsCategorias.append("dataFim", endDate);

        const dadosCategorias = await fetch('/analise/categorias/por-data?' + paramsCategorias.toString())
            .then(res => res.json());

        dadosCategorias.forEach(categoria => {
            // Preencher labels apenas se ainda estiverem vazias
            if (labels.length === 0 && categoria.dados.length > 0) {
                categoria.dados.forEach(ponto => labels.push(ponto.label));
            }

            datasets.push({
                label: `Categoria: ${categoria.nome}`,
                data: categoria.dados.map(p => p.total),
                borderColor: gerarCor(),
                borderDash: [5, 5], // estilo tracejado para categorias
                fill: false
            });
        });
    }

    if (window.graficoVendasInstance) {
        window.graficoVendasInstance.destroy();
    }

    window.graficoVendasInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Análise de Vendas ao Longo do Tempo'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Data'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Quantidade de Vendas'
                    },
                    beginAtZero: true
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
