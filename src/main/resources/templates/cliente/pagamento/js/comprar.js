// Variáveis para controle dos cupons
let totalCompra = 0;
let totalDesconto = 0;
let cuponsAplicados = [];
let livroSelecionado = null; // 🔴 agora salva o livro para não buscar de novo
let quantidadeSelecionada = 1;

document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    quantidadeSelecionada = parseInt(urlParams.get("quantidade")) || 1;
    const clienteId = window.clienteId;

    if (!livroId || !clienteId) {
        alert("Erro ao carregar informações do pagamento.");
        return;
    }

    // Buscar livro
    try {
        livroSelecionado = await fetch(`/livros/${livroId}`).then(res => res.json());
    } catch (error) {
        console.error("Erro ao buscar livro:", error);
        alert("Erro ao buscar informações do livro.");
        return;
    }

    // Atualizar visual do livro
    const cartContainer = document.querySelector(".cart-container");
    cartContainer.innerHTML = `
        <div class="cart-item">
            <div class="cart-info">
                <h3>${livroSelecionado.nome} - ${livroSelecionado.autor.nome}</h3>
                <p class="quantidade">Quantidade: ${quantidadeSelecionada}</p>
                <p class="price">R$ ${livroSelecionado.precoVenda.toFixed(2).replace('.', ',')}</p>
            </div>
            <img src="${livroSelecionado.caminhoImagem}" alt="${livroSelecionado.nome}">
        </div>
    `;

    // Atualizar resumo do pedido
    const resumoCompra = document.querySelectorAll(".cart-summary")[1];

    const precoUnitario = livroSelecionado.precoVenda;
    const subtotal = precoUnitario * quantidadeSelecionada;
    const frete = 0;
    const total = subtotal + frete;

    totalCompra = total; // para controle de cupons

    resumoCompra.innerHTML = `
        <button class="coupon-btn" onclick="openModal('couponModal')">Usar Cupom</button>
        <h4>
            <span>
                R$ ${precoUnitario.toFixed(2).replace('.', ',')}
                ${quantidadeSelecionada > 1 ? ` × ${quantidadeSelecionada}` : ''}
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
    cartoesContainer.innerHTML = "";

    cliente.cartoes.forEach((cartao, index) => {
        const final = cartao.numeroCartao.slice(-4);
        const id = `cartao${index + 1}`;

        const div = document.createElement("div");
        div.classList.add("cartao-item");

        div.innerHTML = `
            <input type="checkbox" id="${id}" name="cartoes" value="${cartao.id}">
            <label for="${id}">Cartão **** **** **** ${final} (${cartao.bandeira.nome})</label>

            <input type="number" id="valor${index + 1}" placeholder="Valor" min="0.01" step="0.01" 
            inputmode="decimal" 
            pattern="^\d+(\.\d{0,2})?$" 
            oninput="oninput="
                this.value = this.value
                .replace(',', '.')
                .replace(/[^0-9.]/g, '')
                .replace(/(\..*)\./g, '$1')">

                `;


        cartoesContainer.appendChild(div);
    });

    // Popular cupons no <select id="cupomSelect">
    const selectCupom = document.getElementById("cupomSelect");
    selectCupom.innerHTML = ""; // limpa opções anteriores

    if (cliente.cupons && cliente.cupons.length > 0) {
        cliente.cupons.forEach(cupom => {
            const option = document.createElement("option");
            option.value = cupom.id; // aqui manda o id do cupom
            option.textContent = `Cupom de ${cupom.tipo}: - R$ ${cupom.valor.toFixed(2).replace('.', ',')}`;
            option.dataset.valor = cupom.valor; // salva o valor como data-atributo para usar depois
            selectCupom.appendChild(option);
        });
    } else {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "Você não possui cupons disponíveis.";
        selectCupom.appendChild(option);
        selectCupom.disabled = true; // desativa o select se não tiver cupons
    }


});

async function finalizarCompra() {
    const enderecoId = parseInt(document.getElementById("endereco").value);
    if (!enderecoId) {
        alert("Por favor, selecione um endereço de entrega.");
        return;
    }

    const clienteId = window.clienteId;

    if (!clienteId || isNaN(clienteId)) {
        alert("ID do cliente não encontrado.");
        return;
    }

    if (!livroSelecionado) {
        alert("Livro não carregado corretamente.");
        return;
    }

    // Coletar os cartões selecionados e valores
    const cartoesSelecionados = [];
    let somaValoresCartao = 0;
    const inputsCartao = document.querySelectorAll("input[name='cartoes']:checked");

    inputsCartao.forEach((checkbox, index) => {
        const valorInput = document.getElementById(`valor${index + 1}`);
        const valor = parseFloat(valorInput.value);

        if (isNaN(valor) || valor <= 0) {
            alert(`Informe um valor válido para o cartão selecionado.`);
            return;
        }

        cartoesSelecionados.push({
            cartaoId: parseInt(checkbox.value),
            valor: valor
        });

        somaValoresCartao += valor;
    });

    // Verifica se o valor informado cobre o necessário
    const totalEsperado = (totalCompra - totalDesconto).toFixed(2);
    if (somaValoresCartao.toFixed(2) !== totalEsperado) {
        alert(`A soma dos valores dos cartões deve ser exatamente R$ ${totalEsperado.replace('.', ',')}`);
        return;
    }

    // 🔽 Montar as vendas (apenas 1 venda com quantidade)
    const vendas = [{
        livroId: livroSelecionado.id,
        quantidade: quantidadeSelecionada
    }];

    // 🔽 Montar o payload
    const pedidoPayload = {
        clienteId: parseInt(clienteId),
        enderecoId: enderecoId,
        vendas: vendas,
        cartoes: cartoesSelecionados,
        cuponsIds: cuponsAplicados.length > 0 ? cuponsAplicados : undefined
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
    
        const data = await response.json();
    
        if (!response.ok) {
            const mensagemErro = data.erro || data.message || "Erro desconhecido ao criar pedido.";
            console.error("Erro da API:", mensagemErro);
            alert("Erro ao criar pedido: " + mensagemErro);
            return;
        }
    
        // Mostrar o modal com o código do pedido
        document.getElementById("pedidoCodigoTexto").textContent = `Seu pedido foi realizado com sucesso!`;
        document.getElementById("pedidoCodigoBold").textContent = `Código: ${data.codigo}`;
    
        openModal('pedidoModal');
    
    } catch (error) {
        console.error("Erro ao finalizar a compra:", error);
        alert("Erro ao finalizar a compra. Tente novamente.");
    }
    
}

function aplicarCupom() {
    let selectElement = document.getElementById("cupomSelect");
    let selectedOption = selectElement.options[selectElement.selectedIndex];
    let cupomId = parseInt(selectedOption.value);
    let cupomValor = parseFloat(selectedOption.dataset.valor);

    if (!cupomId || isNaN(cupomValor)) {
        alert("Selecione um cupom válido.");
        return;
    }

    if (totalCompra - totalDesconto <= 0) {
        alert("Não é possível aplicar mais cupons. O valor da compra já está zerado.");
        return;
    }

    totalDesconto += cupomValor;
    let novoTotal = (totalCompra - totalDesconto).toFixed(2);
    if (novoTotal < 0) {
        novoTotal = 0;
    }

    // Atualiza visualmente o desconto
    let extratoDiv = document.querySelector(".cupom");
    let descontoItem = document.createElement("div");
    descontoItem.innerHTML = `<h4>- R$ ${cupomValor.toFixed(2)} - Cupom</h4>`;
    extratoDiv.insertBefore(descontoItem, extratoDiv.firstChild);

    document.getElementById("total-compra").innerText = `R$ ${parseFloat(novoTotal).toFixed(2).replace('.', ',')}`;

    // Guarda o id do cupom usado
    cuponsAplicados.push(cupomId);

    // Remove o cupom do select
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

function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}