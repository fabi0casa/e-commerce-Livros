function obterParametroPesquisa() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("q");
}

async function buscarLivros() {
    const query = obterParametroPesquisa();
    if (!query) return;

    // Atualiza o título da busca
    const tituloBusca = document.querySelector(".text-container h2");
    if (tituloBusca) {
        tituloBusca.textContent = `Resultados de Busca para "${query}"`;
    }

    try {
        const response = await fetch(`/livros/nome/${encodeURIComponent(query)}`);
        if (!response.ok) throw new Error("Erro ao buscar livros");

        const livros = await response.json();
        const container = document.getElementById("product-list");
        container.innerHTML = ""; // Limpa a lista antes de carregar

        if (livros.length === 0) {
            container.innerHTML = "<p>Nenhum livro encontrado.</p>";
            return;
        }

        livros.forEach(livro => {
            const card = document.createElement("a");
            card.href = `/anuncio?livroId=${livro.id}`;
            card.classList.add("product-card");
            
            card.innerHTML = `
                <img src="${livro.caminhoImagem}" alt="${livro.nome}">
                <div class="product-info">
                    <h3>${livro.nome}</h3>
                    <p class="price">R$ ${livro.precoVenda.toFixed(2)}</p>
                </div>
            `;
            
            container.appendChild(card);
        });
    } catch (error) {
        console.error("Erro ao buscar livros:", error);
    }
}

document.addEventListener("DOMContentLoaded", buscarLivros);
