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

    const dados = {
        livroId: document.getElementById("livro").value,
        quantidadeAdicional: document.getElementById("quantidade").value,
        novoPrecoCusto: document.getElementById("valorCusto").value,
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

// Inicializa
carregarDados();