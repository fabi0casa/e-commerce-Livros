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
    "Em Processamento": ["Aprovada", "Reprovada"],
    "Aprovada": ["Em Transporte"],
    "Em Transporte": ["Entregue"],
    "Troca Solicitada": ["Troca Aceita", "Troca Recusada"],
    "Troca Aceita": ["Troca Concluida"],
    "Devolução Solicitada": ["Devolução Aceita", "Devolução Recusada"],
    "Devolução Aceita": ["Devolução Concluida"]
};

window.addEventListener("DOMContentLoaded", carregarPedidos);

async function carregarPedidos() {
    const response = await fetch("/pedidos/all");
    const pedidos = await response.json();
    const container = document.querySelector(".pedido-list");
    container.innerHTML = "";

    const pedidosMap = new Map();

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
        container.appendChild(div);
    
        // Salva o pedido no Map
        pedidosMap.set(index.toString(), pedido);
    });
    
    // Depois de criar os botões:
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
            <td class="status-cell"><span class="status-btn ${getClass(primeiro.status)}">${primeiro.status}</span></td>
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
                                    <td class="status-cell"><span class="status-btn ${getClass(v.status)}">${v.status}</span></td>
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

    openModal("modalTabela");
}

function gerarBotoes(statusAtual, vendaIds) {
    const transicoes = statusTransicoes[statusAtual] || [];
    return transicoes.map(novo => `
        <button class="status-btn ${getClass(novo)}" onclick="confirmarMudancaStatus(event, '${novo}', ${JSON.stringify(vendaIds)})">${novo}</button>
    `).join(" ");
}

function confirmarMudancaStatus(event, status, vendaIds) {
    event.stopPropagation();
    if (!confirm(`Deseja realmente alterar o status para '${status}'?`)) return;

    vendaIds.forEach(async id => {
        const res = await fetch(`/venda/${id}/status`, {
            method: "PATCH",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status })
        });
        if (res.ok) carregarPedidos();
    });
}

function getClass(status) {
    switch (status) {
        case "Em Processamento": return "processando";
        case "Aprovada": return "aprovado";
        case "Reprovada": return "reprovado";
        case "Em Transporte": return "transito";
        case "Entregue": return "entregue";
        case "Troca Solicitada": return "troca";
        case "Troca Aceita": return "troca-autorizada";
        case "Troca Concluida": return "finalizado";
        case "Devolução Solicitada": return "troca";
        case "Devolução Aceita": return "troca-autorizada";
        case "Devolução Concluida": return "finalizado";
        default: return "";
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