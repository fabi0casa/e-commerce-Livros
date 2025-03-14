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

function redirecioarAoTransacoes(){
    window.location.href = "/administrador/gerenciar-clientes/transacoes";
}

function loadClientDetails(clienteId) {
    fetch(`/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => openClientModal(cliente))
        .catch(error => console.error("Erro ao buscar detalhes do cliente:", error));
}

function openClientModal(cliente) {
    document.getElementById("modalNome").textContent = cliente.nome;
    document.getElementById("modalEmail").textContent = cliente.email;
    document.getElementById("modalTelefone").textContent = cliente.telefone;
    document.getElementById("modalNascimento").textContent = cliente.nascimento;
    document.getElementById("modalGenero").textContent = cliente.genero;
    document.getElementById("modalCpf").textContent = cliente.cpf;
    document.getElementById("modalRanking").textContent = cliente.ranking;

    let btnEnderecos = document.getElementById("btnVerEnderecos");
    let btnCartoes = document.getElementById("btnVerCartoes");

    btnEnderecos.onclick = () => openAddressModal(cliente.enderecos || []);
    btnCartoes.onclick = () => openCardModal(cliente.cartoes || []);

    openModal("clientModal");
}

function openAddressModal(enderecos) {
    let enderecosContainer = document.getElementById("enderecosContainer");
    enderecosContainer.innerHTML = ""; 

    enderecos.forEach(endereco => {
        let button = document.createElement("button");
        button.className = "accordion";
        button.textContent = endereco.fraseIdentificadora || "Endereço";

        let panel = document.createElement("div");
        panel.className = "panel";
        panel.innerHTML = `
            <p><strong>Tipo:</strong> ${endereco.tipo}</p>
            <p><strong>Logradouro:</strong> ${endereco.logradouro}</p>
            <p><strong>Número:</strong> ${endereco.numero}</p>
            <p><strong>Bairro:</strong> ${endereco.bairro}</p>
            <p><strong>CEP:</strong> ${endereco.cep}</p>
            <p><strong>Cidade:</strong> ${endereco.cidade}</p>
            <p><strong>Estado:</strong> ${endereco.estado}</p>
            <p><strong>País:</strong> ${endereco.pais}</p>
            <p><strong>Observação:</strong> ${endereco.observacao || "Nenhuma"}</p>
        `;

        button.addEventListener("click", () => {
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });

        enderecosContainer.appendChild(button);
        enderecosContainer.appendChild(panel);
    });

    openModal("addressModal");
}

function openCardModal(cartoes) {
    let cartoesContainer = document.getElementById("cartoesContainer");
    cartoesContainer.innerHTML = ""; 

    cartoes.forEach(cartao => {
        let button = document.createElement("button");
        button.className = "accordion";
        button.textContent = `**** **** **** ${cartao.numero.slice(-4)}`;

        let panel = document.createElement("div");
        panel.className = "panel";
        panel.innerHTML = `
            <p><strong>Nome Impresso:</strong> ${cartao.nomeImpresso}</p>
            <p><strong>Bandeira:</strong> ${cartao.bandeira}</p>
            <p><strong>Código de Segurança:</strong> ${cartao.codigoSeguranca}</p>
            <p><strong>Preferencial:</strong> ${cartao.preferencial ? "Sim" : "Não"}</p>
        `;

        button.addEventListener("click", () => {
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });

        cartoesContainer.appendChild(button);
        cartoesContainer.appendChild(panel);
    });

    openModal("cardModal");
}


let clienteIdSelecionado = null; // Variável global para armazenar o ID do cliente

// Abre o modal de confirmação e define o ID e nome do cliente
function openConfirmModal(button) {
    clienteIdSelecionado = button.getAttribute("data-id"); // Obtém o ID do cliente
    const clienteNome = button.getAttribute("data-nome"); // Obtém o nome do cliente

    document.getElementById("clienteNome").textContent = clienteNome; // Exibe o nome no modal
    openModal('confirmModal'); // Abre o modal
}

// Função para deletar o cliente
function inativarCliente() {
    if (!clienteIdSelecionado) {
        alert("Erro: Cliente não encontrado.");
        return;
    }

    fetch(`/clientes/delete/${clienteIdSelecionado}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            alert("Cliente excluído com sucesso.");
            window.location.reload(); // Atualiza a página para refletir a mudança
        } else {
            alert("Erro ao excluir cliente.");
        }
    })
    .catch(error => console.error("Erro:", error));
}

