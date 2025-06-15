function obterParametroPesquisa() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("q");
}

async function buscarLivros() {
    const query = obterParametroPesquisa();
    if (!query) return;

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

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
            card.classList.add("product-card-buscar");
        
            const categorias = livro.categorias.map(cat => cat.nome).join(", ");
        
            card.innerHTML = `
                <img src="${livro.caminhoImagem}" alt="${livro.nome}">
                <div class="product-info">
                    <h3>${livro.nome}</h3>
                    <div class="price">R$ ${livro.precoVenda.toFixed(2)}</div>
                    <div class="details">
                        <strong>Autor:</strong> ${livro.autor?.nome || "N/A"}<br>
                        <strong>Editora:</strong> ${livro.editora?.nome || "N/A"}<br>
                        <strong>Ano:</strong> ${livro.anoPublicacao}<br>
                        <strong>Edição:</strong> ${livro.edicao}<br>
                        <strong>Páginas:</strong> ${livro.numPaginas}<br>
                        <strong>Categoria:</strong> ${categorias}
                    </div>
                    <div class="sinopse">
                        ${livro.sinopse.length > 200 ? livro.sinopse.substring(0, 200) + "..." : livro.sinopse}
                    </div>
                </div>
            `;
        
            container.appendChild(card);
        });        
    } catch (error) {
        console.error("Erro ao buscar livros:", error);
    }
    loader.style.display = "none";
}

document.addEventListener("DOMContentLoaded", buscarLivros);
