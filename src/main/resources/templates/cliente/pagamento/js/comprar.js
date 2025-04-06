// Variáveis para controle dos cupons
let totalCompra = 0;
let totalDesconto = 0;
let cuponsAplicados = [];

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

    // Atualizar resumo do pedido (na segunda .cart-summary)
    const resumoCompra = document.querySelectorAll(".cart-summary")[1];

    const precoUnitario = livro.precoVenda;
    const subtotal = precoUnitario * quantidade;
    const frete = 10.70;
    const total = subtotal + frete;

    totalCompra = total; // salva para controle dos cupons

    // Atualiza os valores no extrato de compra
    resumoCompra.innerHTML = `
        <button class="coupon-btn" onclick="openModal('couponModal')">Usar Cupom</button>
        <h4>
            <span>
                R$ ${precoUnitario.toFixed(2).replace('.', ',')}
                ${quantidade > 1 ? ` × ${quantidade}` : ''}
            </span>
        </h4>
        <div class="frete">
            <h4><span>+ R$ ${frete.toFixed(2).replace('.', ',')}</span> - Frete</h4>
        </div>
        <div class="cupom"></div>

        <div class="sumario">
            <h4>Total: <span id="total-compra">R$ ${total.toFixed(2).replace('.', ',')}</span></h4>
        </div>

        <button class="checkout-btn" type="submit" onclick="finalizarCompra()">Finalizar Compra</button>
    `;

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
    for (let i = 0; i < quantidade; i++) {
        vendas.push({
            livroId: livroId,
            valor: precoVenda,
            formaPagamento: "" // preenchida abaixo
        });
    }

    // 🔽 DISTRIBUIR AUTOMATICAMENTE ENTRE OS CARTÕES
    const totalVendas = vendas.reduce((sum, v) => sum + v.valor, 0);
    const valorPorCartao = totalVendas / cartoesSelecionados.length;

    let vendaIndex = 0;

    cartoesSelecionados.forEach((checkbox, index) => {
        const formaPagamento = `Cartão`;
        let valorDistribuido = 0;

        while (
            vendaIndex < vendas.length &&
            valorDistribuido + vendas[vendaIndex].valor <= valorPorCartao + 0.01
        ) {
            vendas[vendaIndex].formaPagamento = `${formaPagamento} - R$ ${vendas[vendaIndex].valor.toFixed(2)}`;
            valorDistribuido += vendas[vendaIndex].valor;
            vendaIndex++;
        }
    });

    // Se sobrar alguma venda, atribui ao primeiro cartão
    for (; vendaIndex < vendas.length; vendaIndex++) {
        const cartaoId = cartoesSelecionados[0].value;
        vendas[vendaIndex].formaPagamento = `Cartão ${cartaoId} - R$ ${vendas[vendaIndex].valor.toFixed(2)}`;
    }

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

// Modal functions (mantém igual)
function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

// 🔄 VERSÃO ORIGINAL DO CUPOM
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
    if (novoTotal < 0) {
        novoTotal = 0;
    }

    // Adiciona visualmente o desconto
    let extratoDiv = document.querySelector(".cupom");
    let descontoItem = document.createElement("div");
    descontoItem.innerHTML = `<h4>- R$ ${cupomValor.toFixed(2)} - Cupom</h4>`;
    extratoDiv.insertBefore(descontoItem, extratoDiv.firstChild);

    document.getElementById("total-compra").innerText = `R$ ${parseFloat(novoTotal).toFixed(2).replace('.', ',')}`;

    // Remove o cupom selecionado
    cuponsAplicados.push(cupomValor);
    selectElement.remove(selectElement.selectedIndex);

    // Fecha modal
    closeModal("couponModal");
}

function redirecionarParaNovoEndereco() {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    const quantidade = urlParams.get("quantidade") || 1;
    window.location.href = `/cliente/pagamento/novoEndereco?livroId=${livroId}&quantidade=${quantidade}`;
}

function redirecionarParaNovoCartao() {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    const quantidade = urlParams.get("quantidade") || 1;
    window.location.href = `/cliente/pagamento/novoCartao?livroId=${livroId}&quantidade=${quantidade}`;
}