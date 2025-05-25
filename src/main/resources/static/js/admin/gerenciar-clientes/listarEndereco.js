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
                        <button onclick="redirecionarEditarEndereco(${endereco.id}, ${clienteId})">Editar</button>
                    </div>
                `;
                listaEnderecos.appendChild(enderecoItem);
            });
            document.getElementById("cadastrarEndereco").href = `/administrador/gerenciar-clientes/cadastrarEndereco?clienteId=${clienteId}`;
        })
        .catch(error => console.error("Erro ao buscar cliente:", error));
});

function redirecionarEditarEndereco(enderecoId, clienteId) {
    window.location.href = `/administrador/gerenciar-clientes/editarEndereco?enderecoId=${enderecoId}&clienteId=${clienteId}`;
}