async function carregarLivros() {
    try {
        const response = await fetch("/livros/all");
        if (!response.ok) throw new Error("Erro ao buscar livros");

        const livros = await response.json();
        const container = document.getElementById("product-list");
        container.innerHTML = ""; // Limpa a lista antes de carregar

        livros.forEach(livro => {
            const card = document.createElement("a");
            card.href = "/cliente/anuncio"; // Pode ser ajustado para um link dinâmico, se necessário
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
        console.error("Erro ao carregar livros:", error);
    }
}

document.addEventListener("DOMContentLoaded", carregarLivros);