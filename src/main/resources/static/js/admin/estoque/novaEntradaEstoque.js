async function carregarDados() {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");

    const endpoints = {
        precificacao: '/grupo-precificacao/lista',
        fornecedor: '/fornecedor/lista'
    };

    // Caso tenha livroId na URL, carrega apenas o nome desse livro
    if (livroId) {
        try {
            const resp = await fetch(`/livros/buscar-nome/${livroId}`);
            if (!resp.ok) throw new Error("Livro não encontrado.");

            const livro = await resp.json();

            const selectLivro = document.getElementById("livro");
            selectLivro.innerHTML = ""; // limpa opções anteriores

            const opt = document.createElement("option");
            opt.value = livroId;
            opt.textContent = livro.nome;
            opt.selected = true;
            selectLivro.appendChild(opt);

            selectLivro.disabled = true; // trava o select
        } catch (err) {
            console.error("Erro ao buscar livro por ID:", err);
            alert("Erro ao carregar o livro selecionado.");
        }
    } else {
        // Caso NÃO tenha livroId, carrega a lista normalmente
        try {
            const resp = await fetch('/livros/lista');
            const dados = await resp.json();

            const select = document.getElementById("livro");
            dados.forEach(item => {
                const opt = document.createElement("option");
                opt.value = item.id;
                opt.textContent = item.nome;
                select.appendChild(opt);
            });
        } catch (err) {
            console.error("Erro ao carregar livros:", err);
            alert("Erro ao carregar a lista de livros.");
        }
    }

    // Carrega os demais selects (grupo de precificação e fornecedor)
    for (const [campo, url] of Object.entries(endpoints)) {
        try {
            const resp = await fetch(url);
            const dados = await resp.json();

            const select = document.getElementById(campo);
            dados.forEach(item => {
                const opt = document.createElement("option");
                opt.value = item.id;
                opt.textContent = item.nome;
                select.appendChild(opt);
            });
        } catch (err) {
            console.error(`Erro ao carregar ${campo}:`, err);
            alert(`Erro ao carregar ${campo}.`);
        }
    }
}

// Envia formulário para o backend
document.getElementById("entradaEstoqueForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    const quantidade = parseInt(document.getElementById("quantidade").value);
    const valorCusto = parseFloat(document.getElementById("valorCusto").value);

    // Validações
    if (isNaN(quantidade) || quantidade < 1) {
        alert("❌ Quantidade deve ser um número inteiro positivo.");
        return;
    }

    if (isNaN(valorCusto) || valorCusto <= 0) {
        alert("❌ Valor de custo deve ser um número positivo maior que zero.");
        return;
    }

    const dados = {
        livroId: document.getElementById("livro").value,
        quantidadeAdicional: quantidade,
        novoPrecoCusto: valorCusto,
        grupoPrecificacaoId: document.getElementById("precificacao").value,
        fornecedorId: document.getElementById("fornecedor").value
    };

    try {
        const resp = await fetch("/livros/estoque/entrada", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dados)
        });

        if (resp.ok) {
            openModal("modalSucesso");
            this.reset();
        } else {
            const erro = await resp.text();
            alert("❌ Erro ao registrar entrada: " + erro);
        }
    } catch (err) {
        console.error("Erro na requisição:", err);
        alert("❌ Erro inesperado. Veja o console.");
    }
    loader.style.display = "none";
});

function cancelar() {
    if (confirm("Deseja cancelar a entrada de estoque?")) {
        window.history.back();
    }
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

document.getElementById("quantidade").addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, ''); // só números

    if (this.value !== '' && parseInt(this.value) < 1) {
        this.value = '';
    }
});

document.getElementById("valorCusto").addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9.,]/g, ''); // só números e vírgula/ponto

    // Troca vírgula por ponto, se necessário
    this.value = this.value.replace(',', '.');

    const valor = parseFloat(this.value);
    if (!isNaN(valor) && valor <= 0) {
        this.value = '';
    }
});

const bloquearTeclasInvalidas = (e) => {
    if (["-", "e", "E"].includes(e.key)) {
        e.preventDefault();
    }
};

document.getElementById("quantidade").addEventListener("keydown", bloquearTeclasInvalidas);
document.getElementById("valorCusto").addEventListener("keydown", bloquearTeclasInvalidas);

// Inicializa
carregarDados();