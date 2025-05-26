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
    window.location.href = "/ver-transacoes-cliente";
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
    document.getElementById("modalNascimento").textContent = formatarData(cliente.dataNascimento);
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
            <p><strong>Observação:</strong> ${endereco.observacoes || "Nenhuma"}</p>
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
        button.textContent = `**** **** **** ${cartao.numeroCartao.slice(-4)}`;

        let panel = document.createElement("div");
        panel.className = "panel";
        panel.innerHTML = `
            <p><strong>Nome Impresso:</strong> ${cartao.nomeImpresso}</p>
            <p><strong>Bandeira:</strong> ${cartao.bandeira ? cartao.bandeira.nome : "Desconhecida"}</p>
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
function openConfirmModal(clienteId, clienteNome) {
    clienteIdSelecionado = clienteId; // Define o ID do cliente globalmente

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


document.addEventListener("DOMContentLoaded", function () {
    // Simula o clique no botão de pesquisa ao carregar a página
    document.getElementById("pesquisar").click();
});

document.getElementById("pesquisar").addEventListener("click", function () {
    let nome = document.getElementById("nome").value;
    let email = document.getElementById("email").value;
    let telefone = document.getElementById("telefone").value;
    let cpf = document.getElementById("cpf").value;
    let dataNascimento = document.getElementById("dataNascimento").value;
    let genero = document.getElementById("genero").value;

    // Converte a string da data para um objeto Date e depois para o formato correto
    if (dataNascimento) {
        let dataObj = new Date(dataNascimento);
        dataNascimento = dataObj.toISOString().split("T")[0]; // Garante formato YYYY-MM-DD
    }

    console.log("Data formatada para envio:", dataNascimento);

    let url = new URL("/clientes/filtro", window.location.origin);
    let params = { nome, email, telefone, cpf, dataNascimento, genero };

    Object.keys(params).forEach(key => {
        if (params[key]) url.searchParams.append(key, params[key]);
    });

    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    console.error("Erro da API:", err);
                    throw new Error("Erro ao buscar clientes.");
                });
            }
            return response.json();
        })
        .then(clientes => {
            console.log("Resposta da API:", clientes);
            if (!Array.isArray(clientes)) {
                console.error("Resposta inesperada da API:", clientes);
                throw new Error("Formato de resposta inválido.");
            }
            atualizarListaClientes(clientes);
        })
        .catch(error => console.error("Erro ao buscar clientes:", error));
});


function atualizarListaClientes(clientes) {
    let clientList = document.getElementById("clientList");
    clientList.innerHTML = ""; // Limpa a lista

    if (clientes.length === 0) {
        clientList.innerHTML = "<p>Nenhum cliente encontrado.</p>";
        return;
    }

    clientes.forEach(cliente => {
        let div = document.createElement("div");
        div.classList.add("client-item");

        div.innerHTML = `
            <div class="client-info">
                <div class="client-name">${cliente.nome}</div>
                <div class="client-details">
                    ${cliente.email} - ${cliente.telefone} - ${formatarData(cliente.dataNascimento)}
                    ${cliente.admin ? '<span class="isAdmin">✪ Administrador</span>' : ''}
                </div>
            </div>
            <div class="client-actions">
                <button onclick="loadClientDetails(${cliente.id})">Ver</button>
                <button onclick="openEditionModal(${cliente.id})">Editar</button>
                <button onclick="openConfirmModal(${cliente.id}, '${cliente.nome}')">Inativar</button>
                <button class="transacoes-btn" data-id="${cliente.id}">Ver Transações</button>
            </div>
        `;

        clientList.appendChild(div);
    });

    // Adiciona evento para os botões de transação
    document.querySelectorAll(".transacoes-btn").forEach(botao => {
        botao.addEventListener("click", function () {
            const clienteId = this.getAttribute("data-id");
            window.location.href = `/ver-transacoes-cliente?clienteId=${clienteId}`;
        });
    });
}

// Função para formatar a data corretamente (DD/MM/YYYY)
function formatarData(data) {
    if (!data) return "";
    let partes = data.split("-");
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".transacoes-btn").forEach(botao => {
        botao.addEventListener("click", function () {
            const clienteId = this.getAttribute("data-id");
            window.location.href = `/ver-transacoes-cliente?clienteId=${clienteId}`;
        });
    });
});
function openEditionModal(clienteId) {
    // Atualiza os links do modal com o ID do cliente
    document.getElementById("editarDadosLink").href = `/editar-cliente?clienteId=${clienteId}`;
    document.getElementById("alterarSenhaLink").href = `/alterar-senha?-clienteclienteId=${clienteId}`;
    document.getElementById("editarEnderecoLink").href = `/enderecos-cliente?clienteId=${clienteId}`;
    document.getElementById("cadastrarCartaoLink").href = `/criar-cartao-cliente?clienteId=${clienteId}`;

    // Abre o modal
    openModal('editionModal');
}

document.addEventListener("DOMContentLoaded", function () {
    let inputData = document.getElementById("dataNascimento");
    inputData.setAttribute("value", ""); // Evita exibir uma data automática

    inputData.addEventListener("focus", function () {
        this.type = "date"; // Altera para `date` quando clicar
    });

    inputData.addEventListener("blur", function () {
        if (!this.value) {
            this.type = "text"; // Volta para `text` se estiver vazio
            this.placeholder = "Data de Nascimento"; // Define o placeholder
        }
    });

    inputData.dispatchEvent(new Event("blur")); // Aplica no carregamento
});

