function checkZoomAndSize() {
    let table = document.getElementById("myTable");

    // Condições para reduzir a fonte
    let isZoomed = window.devicePixelRatio >= 1.75;
    let isSmallScreen = window.innerWidth <= 1100; // Ajuste conforme necessário

    if (isZoomed || isSmallScreen) {
        table.classList.add("zoomed");
    } else {
        table.classList.remove("zoomed");
    }
}

// Checa ao carregar e quando a tela redimensiona
window.addEventListener("load", checkZoomAndSize);
window.addEventListener("resize", checkZoomAndSize);

const statusTransicoes = {
    "Em Processamento": ["Aprovado", "Reprovado"],
    "Aprovado": ["Em Transporte"],
    "Em Transporte": ["Entregue"],
    "Troca Solicitada": ["Troca Aceita", "Troca Recusada"],
    "Troca Aceita": ["Troca Concluida"],
    "Devolução Solicitada": ["Devolução Aceita", "Devolução Recusada"],
    "Devolução Aceita": ["Devolução Concluida"]
};

const statusLabels = {
    "Aprovado": "Aprovar",
    "Reprovado": "Reprovar",
    "Em Transporte": "Despachar",
    "Entregue": "Confirmar Entrega",
    "Troca Aceita": "Aceitar Troca",
    "Troca Recusada": "Recusar Troca",
    "Troca Concluida": "Concluir Troca",
    "Devolução Aceita": "Aceitar Devolução",
    "Devolução Recusada": "Recusar Devolução",
    "Devolução Concluida": "Concluir Devolução"
};

window.addEventListener("DOMContentLoaded", carregarPedidos);

const pedidosMap = new Map();
const listaContainer = document.querySelector(".pedido-list");
const mensagemContainer = document.getElementById("mensagem");

document.getElementById("pesquisar").addEventListener("click", async () => {
    const codigo = document.getElementById("codigo").value.trim();

    if (codigo === "") {
        carregarPedidos(); // mostra todos
    } else {
        try {
            const response = await fetch(`/pedidos/codigo/${codigo}`);
            if (!response.ok) {
                throw new Error("Pedido não encontrado");
            }

            const pedido = await response.json();
            renderizarPedidos([pedido]);
            mensagemContainer.textContent = ""; // limpa mensagem
        } catch (err) {
            listaContainer.innerHTML = "";
            mensagemContainer.textContent = `Nenhum pedido com o código "${codigo}" foi encontrado.`;
        }
    }
});

async function carregarPedidos() {
    const response = await fetch("/pedidos/all");
    const pedidos = await response.json();
    renderizarPedidos(pedidos);
    mensagemContainer.textContent = ""; // limpa mensagem
}

function renderizarPedidos(pedidos) {
    listaContainer.innerHTML = "";
    pedidosMap.clear();

    pedidos.forEach((pedido, index) => {
        const div = document.createElement("div");
        div.className = "pedido-item";

        const raw = pedido.codigo.substring(0, 8);
        const ano = parseInt(raw.substring(0, 4));
        const mes = parseInt(raw.substring(4, 6)) - 1;
        const dia = parseInt(raw.substring(6, 8));
        const dataPedido = new Date(ano, mes, dia).toLocaleDateString();

        const info = `
            <div class="pedido-info">
                <div class="pedido-name">Pedido Nº ${pedido.codigo}</div>
                <div class="pedido-details">${dataPedido} - ${pedido.vendas.length} produto(s)</div>
            </div>
            <div class="pedido-actions">
                <button class="ver-pedido-btn" data-id="${index}">Ver</button>
            </div>
        `;
        div.innerHTML = info;
        listaContainer.appendChild(div);

        pedidosMap.set(index.toString(), pedido);
    });

    // Eventos dos botões "Ver"
    document.querySelectorAll(".ver-pedido-btn").forEach(btn => {
        btn.addEventListener("click", e => {
            const id = e.currentTarget.dataset.id;
            const pedido = pedidosMap.get(id);
            mostrarPedido(pedido);
        });
    });
}

function mostrarPedido(pedido) {
    document.querySelector("#modalTabela h2").innerText = `Pedido Nº ${pedido.codigo}`;
    document.getElementById("modalNome").innerText = pedido.cliente.nome;

    const raw = pedido.codigo.substring(0, 8); // ex: "20250421"
    const ano = parseInt(raw.substring(0, 4));
    const mes = parseInt(raw.substring(4, 6)) - 1; // meses começam do 0 no JS
    const dia = parseInt(raw.substring(6, 8));
    const dataPedido = new Date(ano, mes, dia).toLocaleDateString();

    document.getElementById("modalData").innerText = dataPedido;
    document.getElementById("modalValor").innerText = pedido.valor;
    document.getElementById("modalPagamento").innerText = pedido.formaPagamento;
    document.getElementById("modalEndereco").innerText = pedido.endereco?.fraseIdentificadora || "Endereço não disponível";


    const tabelaBody = document.querySelector("#corpoTabelaPedido");
    tabelaBody.innerHTML = "";

    const livrosMap = new Map();
    pedido.vendas.forEach(venda => {
        const key = venda.livro.id;
        if (!livrosMap.has(key)) {
            livrosMap.set(key, []);
        }
        livrosMap.get(key).push(venda);
    });

    livrosMap.forEach((vendas, livroId) => {
        const primeiro = vendas[0];
        const todosIguais = vendas.every(v => v.status === primeiro.status);

        const row = document.createElement("tr");
        if (vendas.length > 1) row.classList.add("expandable-row");
        row.innerHTML = `
            <td><img id="livro-imagem" src="${primeiro.livro.caminhoImagem}" alt="Capa do livro"></td>
            
            <td>${primeiro.livro.nome}</td>
            <td>R$ ${(primeiro.valor * vendas.length).toFixed(2)}</td>
            <td>${vendas.length}</td>

            <td class="status-cell">${todosIguais ? `<span class="status-btn ${getClass(primeiro.status)}">${primeiro.status}</span>` : 
                `<span class="status-btn indefinido">Status distintos entre os itens</span>`}</td>

            <td>${todosIguais ? gerarBotoes(primeiro.status, vendas.map(v => v.id)) : ""}</td>
        `;
        if (vendas.length > 1) {
            row.onclick = () => toggleSubRows(row);
        }
        tabelaBody.appendChild(row);

        if (vendas.length > 1) {
            const subRow = document.createElement("tr");
            subRow.className = "sub-rows hidden";
            subRow.innerHTML = `
                <td colspan="7">
                    <table class="sub-table">
                        <thead>
                            <th>Livro</th>
                            <th>Valor</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </thead>
                        <tbody>
                            ${vendas.map((v, i) => `
                                <tr>
                                    <td>${v.livro.nome} - ${i + 1}º</td>
                                    <td>R$ ${v.valor.toFixed(2)}</td>
                                    <td class="status-cell">
                                        <span class="status-btn ${getClass(v.status)}" 
                                            data-venda-id="${v.id}" 
                                            data-original-status="${v.status}">
                                            ${v.status}
                                        </span>
                                    </td>
                                    <td>${gerarBotoes(v.status, [v.id])}</td>
                                </tr>
                            `).join("")}
                        </tbody>
                    </table>
                </td>
            `;
            tabelaBody.appendChild(subRow);
        }
    });
    // Verifica se todas as vendas do pedido têm o mesmo status
    const primeiroStatus = pedido.vendas[0]?.status;
    const todosStatusIguais = pedido.vendas.every(v => v.status === primeiroStatus);

    // Verifica se todas as vendas são do mesmo livro
    const primeiroLivroId = pedido.vendas[0]?.livro.id;
    const todosLivrosIguais = pedido.vendas.every(v => v.livro.id === primeiroLivroId);

    // Pega a optional-table e define visibilidade
    const optionalTable = document.querySelector(".optional-table");

    if (todosStatusIguais && !todosLivrosIguais) {
        // Mostra a tabela
        optionalTable.style.display = "table";

        // Preenche a célula de status
        optionalTable.rows[1].cells[0].innerHTML = `<span class="status-btn ${getClass(primeiroStatus)}">${primeiroStatus}</span>`;

        // Preenche a célula de ações com os botões possíveis para esse status
        optionalTable.rows[1].cells[1].innerHTML = gerarBotoes(primeiroStatus, pedido.vendas.map(v => v.id));
    } else {
        // Esconde a tabela se os status forem diferentes
        optionalTable.style.display = "none";
    }

    openModal("modalTabela");
}

function gerarBotoes(statusAtual, vendaIds) {
    const transicoes = statusTransicoes[statusAtual] || [];
    return transicoes.map(novo => {
        const label = statusLabels[novo] || novo;
        return `
            <button class="status-btn ${getClass(novo)}" onclick="confirmarMudancaStatus(event, '${novo}', ${JSON.stringify(vendaIds)})">
                ${label}
            </button>
        `;
    }).join(" ");
}

let vendaIdsPendentes = [];
let novoStatusPendentes = "";
let pedidoAtualPendentes = null;

function confirmarMudancaStatus(event, novoStatus, vendaIds) {
    event.stopPropagation();

    // Armazena dados temporariamente
    vendaIdsPendentes = vendaIds;
    novoStatusPendentes = novoStatus;

    // Exibe o texto no modal
    document.getElementById("modalText").innerText = `Deseja realmente alterar o status para '${novoStatus}'?`;

    // Mostra ou oculta o checkbox de "Retornar ao Estoque"
    const checkboxContainer = document.getElementById("checkboxContainer");
    if (novoStatus === "Troca Aceita" || novoStatus === "Devolução Aceita") {
        checkboxContainer.style.display = "block";
    } else {
        checkboxContainer.style.display = "none";
        document.getElementById("retornaEstoque").checked = false;
    }

    // Encontra e guarda o pedido atual
    const codigo = document.querySelector("#modalTabela h2").innerText.replace("Pedido Nº ", "").trim();
    document.querySelectorAll(".ver-pedido-btn").forEach(btn => {
        const id = btn.dataset.id;
        const pedidoTemp = pedidosMap.get(id);
        if (pedidoTemp && pedidoTemp.codigo === codigo) {
            pedidoAtualPendentes = pedidoTemp;
        }
    });

    if (!pedidoAtualPendentes) {
        alert("Erro interno: pedido não encontrado na memória.");
        return;
    }

    openModal("modalConfirmacao");
}

document.getElementById("confirmarBtn").addEventListener("click", async () => {
    const retornaEstoque = document.getElementById("retornaEstoque").checked;

    // Faz uma única chamada para atualizar todos os IDs de uma vez
    const res = await fetch(`/pedidos/vendas/status`, {
        method: "PATCH",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ vendaIds: vendaIdsPendentes, status: novoStatusPendentes, retornarEstoque: retornaEstoque })
    });

    if (res.ok) {
        // Atualiza o status de cada venda no pedidoAtualPendentes localmente
        for (let id of vendaIdsPendentes) {
            let venda = pedidoAtualPendentes.vendas.find(v => v.id === id);
            if (venda) {
                venda.status = novoStatusPendentes;
            }
        }
    } else {
        let errorMessage = `Erro ao atualizar vendas: `;
        try {
            const contentType = res.headers.get("content-type");
            if (contentType && contentType.includes("application/json")) {
                const data = await res.json();
                errorMessage += data.erro || data.message || JSON.stringify(data);
            } else {
                const text = await res.text();
                if (text) errorMessage += text;
            }
        } catch (e) {
            console.error("Erro ao interpretar resposta da API:", e);
        }
        alert(errorMessage);
    }

    closeModal("modalConfirmacao");
    mostrarPedido(pedidoAtualPendentes);
});

function getClass(status) {
    switch (status) {
        case "Em Processamento": return "processando";
        case "Aprovado": return "aprovado";
        case "Reprovado": return "reprovado";
        case "Cancelado": return "reprovado";
        case "Em Transporte": return "transito";
        case "Entregue": return "entregue";
        case "Troca Solicitada": return "troca";
        case "Troca Aceita": return "troca";
        case "Troca Recusada": return "reprovado";
        case "Troca Concluida": return "troca";
        case "Devolução Solicitada": return "devolucao";
        case "Devolução Aceita": return "devolucao";
        case "Devolução Recusada": return "reprovado";
        case "Devolução Concluida": return "devolucao";
        default: return "indefinido";
    }
}

function toggleSubRows(row) {
    const next = row.nextElementSibling;
    if (next && next.classList.contains("sub-rows")) {
        next.classList.toggle("hidden");
    }
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}