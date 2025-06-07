async function carregarDados() {
    const endpoints = {
        livro: '/livros/lista',
        precificacao: '/grupo-precificacao/lista',
        fornecedor: '/fornecedor/lista'
    };

    for (const [campo, url] of Object.entries(endpoints)) {
        try {
            const resp = await fetch(url);
            const dados = await resp.json();

            const select = document.getElementById(campo);
            dados.forEach(item => {
                const opt = document.createElement('option');
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