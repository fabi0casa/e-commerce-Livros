document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    const quantidade = parseInt(urlParams.get("quantidade")) || 1;
    const clienteId = window.clienteId;

    if (!livroId || !clienteId) {
        alert("Erro ao carregar informações do pagamento.");
        return;
    }

    // Buscar livro
    const livro = await fetch(`/livros/${livroId}`).then(res => res.json());

    // Atualizar visual do livro
    const cartContainer = document.querySelector(".cart-container");
    cartContainer.innerHTML = `
        <div class="cart-item">
            <div class="cart-info">
                <h3>${livro.nome} - ${livro.autor.nome}</h3>
                <p class="quantidade">Quantidade: ${quantidade}</p>
                <p class="price">R$ ${livro.precoVenda.toFixed(2).replace('.', ',')}</p>
            </div>
            <img src="${livro.caminhoImagem}" alt="${livro.nome}">
        </div>
    `;

    // Atualizar resumo do pedido
    const subtotal = livro.precoVenda * quantidade;
    const frete = 10.80;
    const total = subtotal + frete;

    document.querySelector(".cart-summary h4 span#total-compra").textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;

    // Buscar cliente
    const cliente = await fetch(`/clientes/${clienteId}`).then(res => res.json());

    // Popular endereços
    const selectEndereco = document.getElementById("endereco");
    cliente.enderecos
    .filter(end => end.entrega === true)
    .forEach(end => {
        const option = document.createElement("option");
        option.value = end.id;
        option.textContent = end.fraseIdentificadora;
        selectEndereco.appendChild(option);
    });


    // Popular cartões
    const cartoesContainer = document.getElementById("cartoes-container");
    cartoesContainer.innerHTML = ""; // limpar cartões fictícios

    cliente.cartoes.forEach((cartao, index) => {
        const final = cartao.numeroCartao.slice(-4);
        const id = `cartao${index + 1}`;

        const div = document.createElement("div");
        div.classList.add("cartao-item");

        div.innerHTML = `
            <input type="checkbox" id="${id}" name="cartoes" value="${cartao.id}">
            <label for="${id}">Cartão **** **** **** ${final} (${cartao.bandeira.nome})</label>
            <input type="number" id="valor${index + 1}" placeholder="Valor" min="1" step="0.01">
        `;

        cartoesContainer.appendChild(div);
    });
});

async function finalizarCompra() {
    const enderecoId = parseInt(document.getElementById("endereco").value);
    if (!enderecoId) {
        alert("Por favor, selecione um endereço de entrega.");
        return;
    }

    const cartoesSelecionados = Array.from(document.querySelectorAll("input[name='cartoes']:checked"));
    if (cartoesSelecionados.length === 0) {
        alert("Por favor, selecione pelo menos um cartão.");
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const livroId = parseInt(urlParams.get("livroId"));
    const quantidade = parseInt(urlParams.get("quantidade")) || 1;
    const clienteId = window.clienteId;

    if (!clienteId || isNaN(clienteId)) {
        alert("ID do cliente não encontrado.");
        return;
    }

    if (!livroId || isNaN(livroId)) {
        alert("ID do livro inválido.");
        return;
    }

    // 🔽 BUSCAR DADOS DO LIVRO
    let precoVenda;
    try {
        const livroResp = await fetch(`/livros/${livroId}`);
        if (!livroResp.ok) throw new Error("Livro não encontrado.");

        const livro = await livroResp.json();
        precoVenda = livro.precoVenda;

        if (isNaN(precoVenda)) throw new Error("Preço de venda inválido.");
    } catch (error) {
        console.error("Erro ao buscar o livro:", error);
        alert("Erro ao buscar informações do livro.");
        return;
    }

    // 🔽 MONTAR AS VENDAS COM BASE NA QUANTIDADE
    const vendas = [];

    cartoesSelecionados.forEach((checkbox, index) => {
        const valorInput = document.getElementById(`valor${index + 1}`);
        const valor = parseFloat(valorInput.value);
        if (isNaN(valor) || valor <= 0) {
            alert("Informe um valor válido para cada cartão selecionado.");
            throw new Error("Valor inválido");
        }

        for (let i = 0; i < quantidade; i++) {
            vendas.push({
                formaPagamento: `Cartão ${checkbox.value} - R$ ${valor.toFixed(2)}`,
                livroId: livroId,
                valor: precoVenda
            });
        }
    });

    const pedidoPayload = {
        clienteId: parseInt(clienteId),
        enderecoId: enderecoId,
        vendas: vendas
    };

    console.log("Enviando pedido:", pedidoPayload);

    try {
        const response = await fetch("/pedidos/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(pedidoPayload)
        });

        if (!response.ok) {
            const erro = await response.text();
            console.error("Erro da API:", erro);
            throw new Error("Erro ao criar pedido.");
        }

        const pedidoCriado = await response.json();
        alert("Pedido criado com sucesso!");
        window.location.href = "/cliente/conta";
    } catch (error) {
        console.error("Erro ao finalizar a compra:", error);
        alert("Erro ao finalizar a compra.");
    }
}








function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

function aplicarCupom() {
    const cupom = parseFloat(document.getElementById("cupomSelect").value);
    const totalSpan = document.getElementById("total-compra");
    let total = parseFloat(totalSpan.textContent.replace("R$ ", "").replace(",", "."));

    total = Math.max(total - cupom, 0);
    totalSpan.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;

    document.querySelector(".cupom").innerHTML = `<h4>Cupom aplicado: - R$ ${cupom.toFixed(2).replace('.', ',')}</h4>`;
    closeModal("couponModal");
}



/*
let totalCompra = 61.70; // Valor total original
let totalDesconto = 0; // Total de cupons usados
let cuponsAplicados = []; // Lista de cupons usados

function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

function aplicarCupom() {
    let selectElement = document.getElementById("cupomSelect");
    let cupomValor = parseFloat(selectElement.value);
    let cupomTexto = selectElement.options[selectElement.selectedIndex].text;

    if (totalCompra - totalDesconto <= 0) {
        alert("Não é possível aplicar mais cupons. O valor da compra já está zerado.");
        return;
    }

    totalDesconto += cupomValor;
    let novoTotal = (totalCompra - totalDesconto).toFixed(2);
    // Se o total for menor que 0, zera ele
    if (novoTotal < 0) {
        novoTotal = 0;
    }

    // Adicionar visualmente o desconto ao extrato
    let extratoDiv = document.querySelector(".cupom");
    let descontoItem = document.createElement("div");
    descontoItem.innerHTML = `<h4>- R$ ${cupomValor.toFixed(2)} - Cupom</h4>`;
    extratoDiv.insertBefore(descontoItem, extratoDiv.firstChild);

    document.getElementById("total-compra").innerText = `R$ ${novoTotal}`;

    // Remover o cupom já utilizado do select
    cuponsAplicados.push(cupomValor);
    selectElement.remove(selectElement.selectedIndex);

    // Fechar modal
    closeModal("couponModal");
}

function toggleInput(id) {
    let input = document.getElementById(id);
    input.style.display = input.style.display === 'none' ? 'inline-block' : 'none';
}
*/