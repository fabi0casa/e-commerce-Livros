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


let botaoClicado;
let novoStatus;
let precisaCheckbox = false;

function confirmarAcao(botao, status, checkbox = false) {
    botaoClicado = botao;
    novoStatus = status;
    precisaCheckbox = checkbox;

    document.getElementById("modalText").innerText = `Deseja realmente alterar o status para "${status}"?`;
    document.getElementById("checkboxContainer").style.display = checkbox ? "block" : "none";
    document.getElementById("modalConfirmacao").style.display = "flex";
}

function fecharModal() {
    document.getElementById("modalConfirmacao").style.display = "none";
}

document.getElementById("confirmarBtn").addEventListener("click", function() {
    alterarStatus(botaoClicado, novoStatus);
    fecharModal();
});

function alterarStatus(botao, status) {
    let row = botao.closest("tr");
    let statusCell = row.querySelector(".status-cell");
    let actionsCell = botao.closest("td");

    if (!statusCell || !actionsCell) return;

    statusCell.innerHTML = `<span class="status-btn ${getClass(status)}">${status}</span>`;
    actionsCell.innerHTML = novoBotao(status);
}

function novoBotao(status) {
    switch (status) {
        case "Aprovada":
            return `<button class="status-btn transito" onclick="confirmarAcao(this, 'Em Transporte')">Despachar</button>`;
        case "Reprovada":
            return "";
        case "Em Transporte":
            return `<button class="status-btn entregue" onclick="confirmarAcao(this, 'Entregue')">Confirmar Entrega</button>`;
        case "Entregue":
            return `<span class="status-btn finalizado">Finalizado</span>`;
        case "Trocado":
            return `<span class="status-btn troca-autorizada">Troca Autorizada</span>`;
        default:
            return "";
    }
}

function getClass(status) {
    switch (status) {
        case "Em Processamento": return "processando";
        case "Aprovada": return "aprovado";
        case "Reprovada": return "reprovado";
        case "Em Transporte": return "transito";
        case "Entregue": return "entregue";
        case "Trocado": return "troca";
        default: return "";
    }
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}