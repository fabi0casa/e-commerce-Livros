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

function openClientModal(cliente) {
    document.getElementById("modalNome").textContent = cliente.nome;
    document.getElementById("modalEmail").textContent = cliente.email;
    document.getElementById("modalTelefone").textContent = cliente.telefone;
    document.getElementById("modalNascimento").textContent = cliente.nascimento;
    document.getElementById("modalGenero").textContent = cliente.genero;
    document.getElementById("modalCpf").textContent = cliente.cpf;
    document.getElementById("modalRanking").textContent = cliente.ranking;

    // Adiciona os dados ao botão de endereços
    let btnEnderecos = document.getElementById("btnVerEnderecos");
    btnEnderecos.dataset.enderecos = cliente.enderecos;

    // Adiciona os dados ao botão de cartões
    let btnCartoes = document.getElementById("btnVerCartoes");
    btnCartoes.dataset.cartoes = cliente.cartoes;

    btnEnderecos.onclick = function () {
        console.log("Endereços recebidos:", cliente.enderecos);
        openAddressModal(cliente.endereco || []);
    };

    btnCartoes.onclick = function () {
        openCardModal(cliente.cartoes || []);
    };

    openModal("clientModal");
}


function openAddressModal(enderecos) {
    let enderecosContainer = document.getElementById("enderecosContainer");
    enderecosContainer.innerHTML = ""; // Limpa antes de adicionar novos endereços

    enderecos.forEach(endereco => {
        let button = document.createElement("button");
        button.className = "accordion";
        button.textContent = `${endereco.fraseIdentificadora}`;

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

        button.addEventListener("click", function () {
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });

        enderecosContainer.appendChild(button);
        enderecosContainer.appendChild(panel);
    });

    openModal("addressModal");
}

function openCardModal(cartoes) {
    let cartoesContainer = document.getElementById("cartoesContainer");
    cartoesContainer.innerHTML = ""; // Limpa antes de adicionar novos cartões

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

        button.addEventListener("click", function () {
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });

        cartoesContainer.appendChild(button);
        cartoesContainer.appendChild(panel);
    });

    openModal("cardModal");
}