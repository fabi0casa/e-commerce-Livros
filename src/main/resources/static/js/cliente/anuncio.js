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
        const nomesCategorias = livro.categorias.map(cat => cat.nome).join(", ");
        const labelCategoria = livro.categorias.length === 1 ? "Categoria" : "Categorias";

        // Atualiza os parágrafos já existentes
        const detalhesDiv = document.getElementById("livro-detalhes");
        const paragrafos = detalhesDiv.querySelectorAll("p");

        paragrafos[0].innerHTML = `<strong>Autor:</strong> ${livro.autor.nome}`;
        paragrafos[1].innerHTML = `<strong>Editora:</strong> ${livro.editora.nome}`;
        paragrafos[2].innerHTML = `<strong>Fornecedor:</strong> ${livro.fornecedor.nome}`;
        paragrafos[3].innerHTML = `<strong>Ano de Publicação:</strong> ${livro.anoPublicacao}`;
        paragrafos[4].innerHTML = `<strong>Edição:</strong> ${livro.edicao}`;
        paragrafos[5].innerHTML = `<strong>ISBN:</strong> ${livro.isbn}`;
        paragrafos[6].innerHTML = `<strong>Número de Páginas:</strong> ${livro.numPaginas}`;
        paragrafos[7].innerHTML = `<strong>${labelCategoria}:</strong> ${nomesCategorias}`;

        //calcula o valor do estoque do livro
        const quantidadeTotal = livro.estoque;
        document.getElementById("estoque").textContent = `${quantidadeTotal} unidades em Estoque`;
        document.getElementById("quantidade").max = quantidadeTotal;

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

        // Gera o código de barras
        let codigoBarras = livro.codigoBarras;

        // Se tiver 13 dígitos, corta para 12
        if (codigoBarras.length === 13) {
            codigoBarras = codigoBarras.slice(0, 12);
        }
        
        // Se tiver 12 dígitos, calcula o verificador e usa.
        if (codigoBarras.length === 12) {
            codigoBarras = calcularDigitoEAN13(codigoBarras);
        }
        
        JsBarcode("#barcode", codigoBarras, {
            format: "EAN13",
            lineColor: "#000",
            width: 2,
            height: 50,
            displayValue: true
        });

    } catch (error) {
        console.error("Erro ao carregar livro:", error);
    }
}

function calcularDigitoEAN13(codigo12) {
    if (!/^\d{12}$/.test(codigo12)) {
        throw new Error("Código precisa ter 12 dígitos numéricos.");
    }

    let soma = 0;
    for (let i = 0; i < 12; i++) {
        const num = parseInt(codigo12[i]);
        soma += (i % 2 === 0) ? num : num * 3;
    }
    const resto = soma % 10;
    const digitoVerificador = (resto === 0) ? 0 : (10 - resto);
    return codigo12 + digitoVerificador;
}

async function adicionarAoCarrinho() {
    const params = new URLSearchParams(window.location.search);
    const livroId = params.get("livroId");
    const quantidade = document.getElementById("quantidade").value;

    if (!livroId || !quantidade || quantidade < 1) {
        alert("Quantidade inválida ou livro não encontrado.");
        return;
    }

    try {
        const response = await fetch("/carrinho/adicionar", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ livroId, quantidade }) // sem clienteId
        });

        if (response.ok) {
            alert("Livro adicionado ao carrinho com sucesso!");
            window.location.href = "/carrinho";
        } else if (response.status === 401) {
            alert("É necessário estar logado para adicionar itens ao carrinho.");
            window.location.href = "/login";
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
    window.location.href = `/pagamento?livroId=${livroId}&quantidade=${quantidade}`;
}

// Chama a função quando a página carregar
document.addEventListener("DOMContentLoaded", carregarLivro);