let totalCompra = 0;
let totalDesconto = 0;
let cuponsAplicados = [];
let carrinhoItens = [];

document.addEventListener("DOMContentLoaded", async function () {

    // 🔽 Buscar itens do carrinho
    try {
        const response = await fetch(`/carrinho/me`);
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
                        <h3>${livro.nome} - ${livro.autor}</h3>
                        <p class="quantidade">Quantidade: ${quantidade}</p>
                        <p class="price">R$ ${preco.toFixed(2).replace('.', ',')}</p>
                    </div>
                    <img src="${livro.caminhoImagem}" alt="${livro.nome}">
                </div>
            `;
        });

        const frete = 0;
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
        const cliente = await fetch(`/clientes/me`).then(res => res.json());

        const selectEndereco = document.getElementById("endereco");
        cliente.enderecos.filter(e => e.entrega).forEach(end => {
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

                <input type="number" id="valor-cartao-${index + 1}" placeholder="Valor" min="0.01" step="0.01" 
                inputmode="decimal" 
                pattern="^\d+(\.\d{0,2})?$" 
                data-cartao-id="${cartao.id}"
                oninput="oninput="
                    this.value = this.value
                    .replace(',', '.')
                    .replace(/[^0-9.]/g, '')
                    .replace(/(\..*)\./g, '$1')">
                    
                    `;
            cartoesContainer.appendChild(div);
        });

        // Popular cupons
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

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    // Coletar os cartões selecionados e valores
    const cartoesSelecionados = [];
    let somaValoresCartao = 0;
    
    const inputsCartao = document.querySelectorAll("input[name='cartoes']:checked");
    
    inputsCartao.forEach((checkbox) => {
        const cartaoId = checkbox.value;
        const valorInput = document.querySelector(`input[data-cartao-id="${cartaoId}"]`);
    
        const valor = parseFloat(valorInput.value.replace(',', '.'));
    
        if (isNaN(valor) || valor < 10.00) {
            alert(`Informe um valor válido (mínimo R$ 10,00) para o cartão selecionado.`);
            loader.style.display = "none";
            return;
        }
    
        cartoesSelecionados.push({
            cartaoId: parseInt(cartaoId),
            valor: valor
        });
    
        somaValoresCartao += valor;
    });
    
    const totalEsperado = (totalCompra - totalDesconto).toFixed(2);
    if (somaValoresCartao.toFixed(2) !== totalEsperado) {
        alert(`A soma dos valores dos cartões deve ser exatamente R$ ${totalEsperado.replace('.', ',')}`);
        loader.style.display = "none";
        return;
    }

    // 🔽 Montar o payload baseado no PedidoCarrinhoRequest
    const pedidoPayload = {
        enderecoId: enderecoId,
        cartoes: cartoesSelecionados,
        cuponsIds: cuponsAplicados.length > 0 ? cuponsAplicados : undefined
    };

    console.log("Enviando pedido do carrinho:", pedidoPayload);

    try {
        const response = await fetch("/pedidos/add-carrinho", {
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
            loader.style.display = "none";
            return;
        }

        document.getElementById("pedidoCodigoTexto").textContent = `Seu pedido foi realizado com sucesso!`;
        document.getElementById("pedidoCodigoBold").textContent = `Código: ${data.codigo}`;
        openModal('pedidoModal');

    } catch (error) {
        console.error("Erro ao finalizar compra do carrinho:", error);
        alert("Erro ao processar sua compra. Tente novamente.");
    }
    loader.style.display = "none";
}

// Modal functions (mantém igual)
function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
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