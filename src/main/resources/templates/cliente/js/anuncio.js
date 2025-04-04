async function carregarLivro() {
    // Captura o ID do livro da URL
    const params = new URLSearchParams(window.location.search);
    const livroId = params.get("livroId");

    if (!livroId) {
        console.error("ID do livro não encontrado na URL.");
        return;
    }

    try {
        // Faz a requisição ao backend para buscar os detalhes do livro
        const response = await fetch(`/livros/${livroId}`);
        if (!response.ok) throw new Error("Erro ao buscar dados do livro");

        const livro = await response.json();

        // Atualiza o título da página
        document.title = livro.nome;

        // Atualiza a imagem do livro
        document.getElementById("livro-imagem").src = livro.caminhoImagem;
        document.getElementById("livro-imagem").alt = `Capa do livro ${livro.nome}`;

        // Atualiza os detalhes principais
        document.getElementById("livro-nome").textContent = livro.nome;
        document.getElementById("livro-preco").textContent = `R$ ${livro.precoVenda.toFixed(2)}`;

        // Atualiza a sinopse
        document.getElementById("livro-sinopse").textContent = livro.sinopse;

        // Atualiza os detalhes adicionais
        document.getElementById("livro-detalhes").innerHTML = `
            <p><strong>Autor:</strong> ${livro.autor.nome}</p>
            <p><strong>Editora:</strong> ${livro.editora.nome}</p>
            <p><strong>Fornecedor:</strong> ${livro.fornecedor.nome}</p>
            <p><strong>Ano de Publicação:</strong> ${livro.anoPublicacao}</p>
            <p><strong>Edição:</strong> ${livro.edicao}</p>
            <p><strong>ISBN:</strong> ${livro.isbn}</p>
            <p><strong>Número de Páginas:</strong> ${livro.numPaginas}</p>
        `;

        // Seleciona o elemento da classe "acoes-produto"
        const acoesProduto = document.querySelector(".acoes-produto");

        // Define a margem inicial
        let marginTop = 170;

        // Define o ponto inicial de redução (19 caracteres) e o intervalo de ajuste (18 caracteres)
        const limiteInicial = 19;
        const intervalo = 18;
        const reducaoPorIntervalo = 34; // Diferença entre 170px e 136px

        // Se o nome for maior ou igual ao limite inicial, calcula a nova margem
        if (livro.nome.length >= limiteInicial) {
            // Calcula quantos intervalos acima do limite inicial o nome tem
            const intervalosExtras = Math.floor((livro.nome.length - limiteInicial) / intervalo);
            
            // Reduz a margem proporcionalmente
            marginTop = 136 - (intervalosExtras * reducaoPorIntervalo);
            
            // Define um limite mínimo para a margem para evitar valores negativos
            if (marginTop < 50) marginTop = 50;
        }

        // Aplica o novo valor de margin-top
        acoesProduto.style.marginTop = `${marginTop}px`;

    } catch (error) {
        console.error("Erro ao carregar livro:", error);
    }
}

async function adicionarAoCarrinho() {
    const params = new URLSearchParams(window.location.search);
    const livroId = params.get("livroId"); // Pegando o livroId da URL
    const clienteId = window.clienteId; // Pegando o clienteId do Thymeleaf
    const quantidade = document.getElementById("quantidade").value; // Pegando a quantidade informada pelo usuário

    if (!clienteId) {
        alert("É necessário estar logado para adicionar itens ao carrinho.");
        return;
    }

    if (!livroId || !quantidade || quantidade < 1) {
        alert("Quantidade inválida ou livro não encontrado.");
        return;
    }

    try {
        const response = await fetch("/carrinho/adicionar", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ clienteId, livroId, quantidade })
        });

        if (response.ok) {
            alert("Livro adicionado ao carrinho com sucesso!");
            window.location.href = "/cliente/carrinho";
        } else {
            const errorText = await response.text();
            console.error("Erro ao adicionar ao carrinho:", errorText);
            alert("Não foi possível adicionar ao carrinho.");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Ocorreu um erro ao tentar adicionar ao carrinho.");
    }
}

function comprarLivro() {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    const quantidade = document.getElementById("quantidade").value;

    if (!livroId || !quantidade || quantidade <= 0) {
        alert("ID do livro ou quantidade inválida.");
        return;
    }

    // Redireciona para a rota com os parâmetros
    window.location.href = `/cliente/pagamento/comprar?livroId=${livroId}&quantidade=${quantidade}`;
}

// Chama a função quando a página carregar
document.addEventListener("DOMContentLoaded", carregarLivro);