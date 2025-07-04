document.addEventListener("DOMContentLoaded", function () {
    const loader = document.getElementById("loader");
    loader.style.display = "flex";
    
    // Buscar nome do cliente
    fetch(`/clientes/me/nome`)
        .then(response => {
            if (!response.ok) throw new Error("Erro ao buscar nome do cliente");
            return response.json(); // Agora o retorno é um JSON
        })
        .then(data => {
            document.querySelector("h2").textContent = `Cartões de ${data.nome}`;
        })
        .catch(error => {
            console.error("Erro ao buscar nome:", error);
            document.querySelector("h2").textContent = "Cartões";
        });

    const mensagemContainer = document.getElementById("mensagem");

    // Buscar endereços do cliente
    fetch(`/cartoes/cliente/me`)
        .then(response => {
            if (!response.ok) throw new Error("Erro ao buscar Cartões");
            return response.json();
        })
        .then(cartoes => {
            const listaCartoes = document.querySelector(".client-list");
            listaCartoes.innerHTML = "";

            if (cartoes.length === 0) {
                mensagemContainer.textContent = "Nenhum cartão encontrado.";
                return;
            }
            
            cartoes.forEach(cartao => {
                const cartaoItem = document.createElement("div");
                cartaoItem.classList.add("client-item");
            
                // Container visual do cartão
                const visualId = `visual-cartao-${cartao.id}`;
                cartaoItem.innerHTML = `
                    <div class="client-info">
                    <div class="cartao-visual">
                        <div class="cartao-numero">${cartao.numeroCartao}</div>
                        <div class="cartao-nome">${cartao.nomeImpresso}</div>
                        <div class="cartao-bandeira">${cartao.bandeira ? cartao.bandeira.nome : "Desconhecida"}</div>
                    </div>
                    </div>
                    <div class="client-actions">
                        <button onclick='abrirModalDetalhes(${JSON.stringify(cartao)})'>Detalhes</button>
                        <button onclick="redirecionarEditarCartao(${cartao.id})">Editar</button>
                        <button onclick="abrirConfirmacaoExclusao(${cartao.id}, '${cartao.numeroCartao}')">Excluir</button>
                    </div>
                `;

                const corSecundaria = gerarCorSecundaria(cartao.numeroCartao);
                const cartaoVisual = cartaoItem.querySelector('.cartao-visual');
                cartaoVisual.style.background = `linear-gradient(to right, #001133, ${corSecundaria})`;
            
                listaCartoes.appendChild(cartaoItem);                
            });
            
        })
        .catch(error => console.error("Erro ao buscar cartões:", error))        
        .finally(() => {
            loader.style.display = "none";
        });

    document.getElementById("cadastrarCartao").href = `/novo-cartao?conta=1`;
});

function redirecionarEditarCartao(cartaoId) {
    window.location.href = `/editar-cartao?cartaoId=${cartaoId}`;
}

let cartaoIdSelecionado = null;

function abrirModalDetalhes(cartao) {
    document.getElementById("nomeImpresso").textContent =  cartao.nomeImpresso;
    document.getElementById("numero").textContent = cartao.numeroCartao;
    document.getElementById("bandeira").textContent = cartao.bandeira ? cartao.bandeira.nome : "Desconhecida";
    document.getElementById("codigo").textContent = cartao.codigoSeguranca;
    document.getElementById("preferencial").textContent = cartao.preferencial ? "Sim" : "Não";

    openModal("detalhesModal");
}

function abrirConfirmacaoExclusao(cartaoId, numero) {
    cartaoIdSelecionado = cartaoId;

    const mensagem = `Tem certeza que deseja excluir o cartão de número "${numero}"?`;
    document.querySelector("#confirmDeleteModal p").textContent = mensagem;

    openModal("confirmDeleteModal");
}


function excluirCartaoConfirmado() {
    if (!cartaoIdSelecionado) {
        alert("Cartão não encontrado.");
        return;
    }

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    fetch(`/cartoes/delete/me/${cartaoIdSelecionado}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            alert("Cartão excluído com sucesso.");
            window.location.reload(); // Atualiza lista
        } else {
            alert("Erro ao excluir cartão.");
        }
    })
    .catch(error => console.error("Erro ao excluir:", error))
    .finally(() => {
        loader.style.display = "none";
    });
}

function gerarCorSecundaria(numeroCartao) {
    let hash = 0;
    for (let i = 0; i < numeroCartao.length; i++) {
        hash = numeroCartao.charCodeAt(i) + ((hash << 5) - hash);
    }

    // Gera uma cor entre RGB(100–180)
    const r = 100 + (hash % 100);        // 100–179
    const g = 100 + ((hash >> 2) % 100);
    const b = 100 + ((hash >> 4) % 100);

    return `rgb(${r}, ${g}, ${b})`;
}


function openModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}
