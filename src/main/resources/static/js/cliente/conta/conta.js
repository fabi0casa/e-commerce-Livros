let botaoClicado;
let novoStatus;
let precisaCheckbox = false;

function confirmarAcao(botao, status) {
    botaoClicado = botao;
    novoStatus = status;

    document.getElementById("modalConfirmacao").style.display = "flex";
}

function fecharModal() {
    document.getElementById("modalConfirmacao").style.display = "none";
}

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
        case "Em Troca": return "troca";
        default: return "";
    }
}

function mostrarSecao(secao) {
    document.querySelectorAll('.secao').forEach(el => el.classList.remove('ativa'));
    document.getElementById(secao).classList.add('ativa');
}

document.getElementById("endereco").addEventListener("submit", function (event) {
    let residencia = document.getElementById("residencia").value;
    let entrega = document.getElementById("entrega").value;
    let cobranca = document.getElementById("cobranca").value;

    // Verifica se pelo menos um campo tem "Sim"
    if (residencia === "sim" || entrega === "sim" || cobranca === "sim") {

    } else {
        event.preventDefault();
        openModal('errorModal'); // Exibe o modal de erro
    }
});

function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}
// Script para o efeito de accordion (lingueta que desce)
document.addEventListener("DOMContentLoaded", function () {
    let accordions = document.querySelectorAll(".accordion");

    accordions.forEach(button => {
        button.addEventListener("click", function () {
            let panel = this.nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }
        });
    });
});

function logout() {
    fetch('/sessao/logout', {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/home'; // Redireciona para /home após logout
        } else {
            alert("Erro ao deslogar.");
        }
    })
    .catch(error => {
        console.error("Erro de rede:", error);
    });
}

