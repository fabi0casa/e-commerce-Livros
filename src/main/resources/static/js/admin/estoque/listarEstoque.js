document.addEventListener('DOMContentLoaded', () => {
    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    fetch('/livros/estoque')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar dados do estoque');
            }
            return response.json();
        })
        .then(data => {
            const tbody = document.querySelector('#tabela-estoque tbody');
            tbody.innerHTML = ''; // limpa qualquer conteúdo anterior

            data.forEach(livro => {
                const tr = document.createElement('tr');

                tr.innerHTML = `
                    <td>${livro.nome}</td>
                    <td>${livro.estoque}</td>
                    <td>R$ ${livro.precoCusto.toFixed(2).replace('.', ',')}</td>
                    <td>R$ ${livro.precoVenda.toFixed(2).replace('.', ',')}</td>
                    <td>${livro.grupoPrecificacao}</td>
                    <td>${livro.margemLucro}%</td>
                    <td>${livro.fornecedor}</td>
                    <td class="td-entrada">
                        <a href="/entrada-estoque?livroId=${livro.livroId}" class="botao-entrada">+</a>
                    </td>
                `;

                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error(error);
            alert('Erro ao carregar os dados do estoque.');
        })
        .finally(() => {
            loader.style.display = "none";
        });
});

