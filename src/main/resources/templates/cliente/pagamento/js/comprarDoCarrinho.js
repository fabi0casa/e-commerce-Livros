let totalCompra = 0;
let totalDesconto = 0;
let cuponsAplicados = [];
let carrinhoItens = [];

document.addEventListener("DOMContentLoaded", async function () {
    const clienteId = window.clienteId;
    if (!clienteId) {
        alert("Cliente não identificado.");
        return;
    }

    // 🔽 Buscar itens do carrinho
    try {
        const response = await fetch(`/carrinho/${clienteId}`);
        carrinhoItens = await response.json();

        const cartContainer = document.querySelector(".cart-container");
        const resumoCompra = document.querySelectorAll(".cart-summary")[1];
        cartContainer.innerHTML = "";

        let subtotal = 0;

        carrinhoItens.forEach(item => {
            const livro = item.livro;
            const preco = livro.precoVenda;
            const quantidade = item.quantidade;

            subtotal += preco * quantidade;

            cartContainer.innerHTML += `
                <div class="cart-item">
                    <div class="cart-info">
                        <h3>${livro.nome} - ${livro.autor.nome}</h3>
                        <p class="quantidade">Quantidade: ${quantidade}</p>
                        <p class="price">R$ ${preco.toFixed(2).replace('.', ',')}</p>
                    </div>
                    <img src="${livro.caminhoImagem}" alt="${livro.nome}">
                </div>
            `;
        });

        const frete = 10.70;
        const total = subtotal + frete;
        totalCompra = total;

        resumoCompra.innerHTML = `
            <button class="coupon-btn" onclick="openModal('couponModal')">Usar Cupom</button>
            <h4>Subtotal: R$ ${subtotal.toFixed(2).replace('.', ',')}</h4>
            <div class="frete">
                <h4><span>+ R$ ${frete.toFixed(2).replace('.', ',')}</span> - Frete</h4>
            </div>
            <div class="cupom"></div>
            <div class="sumario">
                <h4>Total: <span id="total-compra">R$ ${total.toFixed(2).replace('.', ',')}</span></h4>
            </div>
            <button class="checkout-btn" type="submit" onclick="finalizarCompra()">Finalizar Compra</button>
        `;

        // Buscar dados do cliente para cartões e endereços
        const cliente = await fetch(`/clientes/${clienteId}`).then(res => res.json());

        const selectEndereco = document.getElementById("endereco");
        cliente.enderecos.filter(e => e.entrega).forEach(end => {
            const option = document.createElement("option");
            option.value = end.id;
            option.textContent = end.fraseIdentificadora;
            selectEndereco.appendChild(option);
        });

        const cartoesContainer = document.getElementById("cartoes-container");
        cartoesContainer.innerHTML = "";
        cliente.cartoes.forEach((cartao, index) => {
            const final = cartao.numeroCartao.slice(-4);
            const id = `cartao${index + 1}`;
            cartoesContainer.innerHTML += `
                <div class="cartao-item">
                    <input type="checkbox" id="${id}" name="cartoes" value="${cartao.id}">
                    <label for="${id}">Cartão **** **** **** ${final} (${cartao.bandeira.nome})</label>
                    <input type="number" id="valor${index + 1}" placeholder="Valor" min="1" step="0.01">
                </div>
            `;
        });

    } catch (error) {
        console.error("Erro ao carregar carrinho:", error);
        alert("Erro ao carregar itens do carrinho.");
    }
});

async function finalizarCompra() {
    const enderecoId = parseInt(document.getElementById("endereco").value);
    if (!enderecoId) {
        alert("Por favor, selecione um endereço.");
        return;
    }

    const cartoesSelecionados = Array.from(document.querySelectorAll("input[name='cartoes']:checked"));
    if (cartoesSelecionados.length === 0) {
        alert("Selecione pelo menos um cartão.");
        return;
    }

    const clienteId = window.clienteId;
    if (!clienteId) {
        alert("Cliente não identificado.");
        return;
    }

    const vendas = [];

    // Etapa 1: montar a lista completa de vendas com valor correto
    carrinhoItens.forEach(item => {
        const { livro, quantidade } = item;
        for (let i = 0; i < quantidade; i++) {
            vendas.push({
                livroId: livro.id,
                valor: livro.precoVenda,
                formaPagamento: "" // será preenchido na etapa 2
            });
        }
    });

    // Etapa 2: distribuir automaticamente entre os cartões
    const totalVendas = vendas.reduce((sum, v) => sum + v.valor, 0);
    const valorPorCartao = totalVendas / cartoesSelecionados.length;

    let vendaIndex = 0;

    cartoesSelecionados.forEach((checkbox, index) => {
        const formaPagamento = `Cartão`;

        let valorDistribuido = 0;

        while (vendaIndex < vendas.length && valorDistribuido + vendas[vendaIndex].valor <= valorPorCartao + 0.01) {
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

    // 🔽 VERIFICAR SOMA DOS VALORES DOS CARTÕES
    let somaCartoes = 0;

    cartoesSelecionados.forEach((checkbox, index) => {
        const inputValor = document.getElementById(`valor${index + 1}`);
        const valor = parseFloat(inputValor.value);
        
        if (isNaN(valor) || valor <= 0) {
        } else {
            somaCartoes += valor;
        }
    });

    const totalComDesconto = totalCompra - totalDesconto;

    if (somaCartoes < totalComDesconto) {
        alert(`A soma dos valores inseridos nos cartões (R$ ${somaCartoes.toFixed(2).replace('.', ',')}) é inferior ao total da compra (R$ ${totalComDesconto.toFixed(2).replace('.', ',')}).`);
        return;
    }
    

    const pedidoPayload = {
        clienteId: clienteId,
        enderecoId: enderecoId,
        vendas: vendas
    };

    try {
        const response = await fetch("/pedidos/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(pedidoPayload)
        });

        if (!response.ok) {
            const erro = await response.text();
            throw new Error("Erro ao criar pedido: " + erro);
        }

        // Após criar pedido, remover itens do carrinho
        for (const item of carrinhoItens) {
            await fetch(`/carrinho/remover/${item.id}`, { method: "DELETE" });
        }

        alert("Compra realizada com sucesso!");
        window.location.href = "/cliente/conta";
    } catch (error) {
        console.error("Erro ao finalizar compra:", error);
        alert("Erro ao processar sua compra.");
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
