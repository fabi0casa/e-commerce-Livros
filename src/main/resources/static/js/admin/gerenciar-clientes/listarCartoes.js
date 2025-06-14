document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const clienteId = urlParams.get("clienteId");
    
    if (!clienteId) {
        alert("ID do cliente não encontrado!");
        return;
    }
    
    fetch(`/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            document.querySelector("h2").textContent = `Gerenciamento de Endereços de ${cliente.nome}`;
            const listaEnderecos = document.querySelector(".client-list");
            listaEnderecos.innerHTML = ""; 

            cliente.enderecos.forEach(endereco => {
                const enderecoItem = document.createElement("div");
                enderecoItem.classList.add("client-item");
            
                enderecoItem.innerHTML = `
                    <div class="client-info">
                        <div class="client-name">${endereco.fraseIdentificadora}</div>
                        <div class="client-details">CEP: ${endereco.cep} - ${endereco.bairro}</div>
                    </div>
                    <div class="client-actions">
                        <button onclick='abrirModalDetalhes(${JSON.stringify(endereco)})'>Detalhes</button>
                        <button onclick="redirecionarEditarEndereco(${endereco.id}, ${clienteId})">Editar</button>
                        <button onclick="abrirConfirmacaoExclusao(${endereco.id})">Excluir</button>
                    </div>
                `;
            
                listaEnderecos.appendChild(enderecoItem);
            });
            
            document.getElementById("cadastrarEndereco").href = `/criar-endereco-cliente?clienteId=${clienteId}`;
        })
        .catch(error => console.error("Erro ao buscar cliente:", error));


    
});

function redirecionarEditarEndereco(enderecoId, clienteId) {
    window.location.href = `/editar-endereco-cliente?enderecoId=${enderecoId}&clienteId=${clienteId}`;
}

let enderecoIdSelecionado = null;

function abrirModalDetalhes(endereco) {
    document.getElementById("detalheTipo").textContent = endereco.tipo;
    document.getElementById("detalheLogradouro").textContent = endereco.logradouro;
    document.getElementById("detalheNumero").textContent = endereco.numero;
    document.getElementById("detalheBairro").textContent = endereco.bairro;
    document.getElementById("detalheCep").textContent = endereco.cep;
    document.getElementById("detalheCidade").textContent = endereco.cidade;
    document.getElementById("detalheEstado").textContent = endereco.estado;
    document.getElementById("detalhePais").textContent = endereco.pais;
    document.getElementById("detalheObservacoes").textContent = endereco.observacoes || "-";

    let finalidades = [];
    if (endereco.residencial) finalidades.push("Residencial");
    if (endereco.entrega) finalidades.push("Entrega");
    if (endereco.cobranca) finalidades.push("Cobrança");

    document.getElementById("detalheFinalidades").textContent = finalidades.join(", ") || "-";

    openModal("detalhesModal");
}

function abrirConfirmacaoExclusao(enderecoId) {
    enderecoIdSelecionado = enderecoId;
    openModal("confirmDeleteModal");
}

function excluirEnderecoConfirmado() {
    if (!enderecoIdSelecionado) {
        alert("Endereço não encontrado.");
        return;
    }

    fetch(`/enderecos/delete/${enderecoIdSelecionado}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            alert("Endereço excluído com sucesso.");
            window.location.reload(); // Atualiza lista
        } else {
            alert("Erro ao excluir endereço.");
        }
    })
    .catch(error => console.error("Erro ao excluir:", error));
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}
