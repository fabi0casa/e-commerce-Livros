document.addEventListener("DOMContentLoaded", function () {
    const clienteId = window.clienteId;
    const cartContainer = document.querySelector(".cart-container");
    const totalSpan = document.querySelector(".cart-summary span");

    function carregarCarrinho() {
        fetch(`/carrinho/${clienteId}`)
            .then(response => response.json())
            .then(data => {
                cartContainer.innerHTML = ""; // limpa
                let total = 0;

                data.forEach(item => {
                    const preco = item.livro.precoVenda;
                    const subtotal = preco * item.quantidade;
                    total += subtotal;

                    const div = document.createElement("div");
                    div.className = "cart-item";
                    div.dataset.carrinhoId = item.id;
                    div.innerHTML = `
                        <img src="${item.livro.caminhoImagem}" alt="${item.livro.nome}">
                        <div class="cart-info">
                            <h3>${item.livro.nome} - ${item.livro.autor.nome}</h3>
                            <p class="price">R$ ${preco.toFixed(2).replace('.', ',')}</p>
                        </div>
                        <div class="cart-actions">
                            <div class="quantity">
                                <button class="decrease">-</button>
                                <span>${item.quantidade}</span>
                                <button class="increase">+</button>
                                <button class="remove">Remover</button>
                            </div>
                        </div>
                    `;
                    cartContainer.appendChild(div);
                });

                totalSpan.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;
                adicionarListeners();
            });
    }

    function adicionarListeners() {
        document.querySelectorAll(".cart-item").forEach(item => {
            const carrinhoId = item.dataset.carrinhoId;
            const decreaseBtn = item.querySelector(".decrease");
            const increaseBtn = item.querySelector(".increase");
            const quantitySpan = item.querySelector(".quantity span");
            const removeBtn = item.querySelector(".remove");

            decreaseBtn.addEventListener("click", () => {
                let current = parseInt(quantitySpan.textContent);
                if (current > 1) {
                    atualizarQuantidade(carrinhoId, current - 1, quantitySpan);
                }
            });

            increaseBtn.addEventListener("click", () => {
                let current = parseInt(quantitySpan.textContent);
                atualizarQuantidade(carrinhoId, current + 1, quantitySpan);
            });

            removeBtn.addEventListener("click", () => {
                fetch(`/carrinho/remover/${carrinhoId}`, {
                    method: 'DELETE'
                }).then(() => location.reload());
            });
        });
    }

    function atualizarQuantidade(carrinhoId, novaQtd, spanElement) {
        fetch(`/carrinho/atualizar/${carrinhoId}?novaQuantidade=${novaQtd}`, {
            method: 'PUT'
        })
        .then(response => response.json())
        .then(() => {
            spanElement.textContent = novaQtd;
            carregarCarrinho(); // atualiza valores do total
        });
    }

    carregarCarrinho();
});