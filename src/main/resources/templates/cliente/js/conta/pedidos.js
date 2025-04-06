document.addEventListener("DOMContentLoaded", function () {
    const container = document.getElementById("contaPedidos");

    fetch(`/pedidos/cliente/${clienteId}`)
        .then(response => response.json())
        .then(data => renderPedidos(data))
        .catch(error => console.error("Erro ao buscar pedidos:", error));

    function renderPedidos(pedidos) {
        let html = `
            <div id="pedidos" class="secao">
                <h2>Meus Pedidos</h2>
                <div class="pedido-list">
        `;

        pedidos.forEach((pedido, index) => {
            const dataPedido = new Date(pedido.vendas[0]?.dataHora || "").toLocaleDateString();
            html += `
                <div class="pedido-item">
                    <div class="pedido-info">
                        <div class="pedido-name">Pedido Nº ${pedido.codigo}</div>
                        <div class="pedido-details">${dataPedido} - ${pedido.vendas.length} produtos</div>
                    </div>
                    <div class="pedido-actions">
                        <button onclick="mostrarVendas(${index})">Ver</button>
                    </div>
                </div>
            `;
        });

        html += `
                </div>
            </div>

            <div id="modalTabela" class="modal-tabela">
                <div class="modal-tabela-content">
                    <span class="close" onclick="closeModal('modalTabela')">&times;</span>
                    <h2 id="tituloPedido"></h2>
                    <table id="myTable">
                        <thead>
                            <tr>
                                <th>Data</th>
                                <th>Livro</th>
                                <th>Valor</th>
                                <th>Forma de Pagamento</th>
                                <th>Endereço</th>
                                <th>Status da Compra</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody id="vendasBody">
                        </tbody>
                    </table>
                </div>
            </div>

            <div id="modalConfirmacao" class="modal">
                <div class="modal-content">
                    <h2>Confirmar Ação</h2>
                    <p>Deseja realmente solicitar a Troca desse produto?</p>
                    <div id="checkboxContainer" style="display: none;"></div>
                    </br>
                    <button id="confirmarBtn">Confirmar</button>
                    <button onclick="fecharModal()">Cancelar</button>
                </div>
            </div>
        `;

        container.innerHTML = html;

        // salvar os pedidos no escopo global para acesso ao clicar em "Ver"
        window.pedidosGlobais = pedidos;

        document.getElementById("confirmarBtn").addEventListener("click", function () {
            alterarStatus(botaoClicado, novoStatus); // precisa estar definido fora
            fecharModal();
        });

        checkZoomAndSize();
        window.addEventListener("resize", checkZoomAndSize);
    }

    window.mostrarVendas = function (index) {
        const pedido = window.pedidosGlobais[index];
        const tbody = document.getElementById("vendasBody");
        const titulo = document.getElementById("tituloPedido");
    
        titulo.innerText = `Pedido Nº ${pedido.codigo}`;
        tbody.innerHTML = "";
    
        pedido.vendas.forEach(venda => {
            const livro = venda.livro;
            const endereco = pedido.endereco.fraseIdentificadora || "Sem endereço";
            const dataFormatada = new Date(venda.dataHora).toLocaleDateString();
            const valor = venda.formaPagamento.split(" - R$ ")[1] || "0,00";
    
            // Define a classe baseada no status
            const status = venda.status.toLowerCase();
            let statusClass = "";
    
            if (status.includes("processamento")) statusClass = "processando";
            else if (status.includes("trânsito")) statusClass = "transito";
            else if (status.includes("entregue")) statusClass = "entregue";
            else if (status.includes("troca")) statusClass = "troca";
            else if (status.includes("aprovado")) statusClass = "aprovado";
            else if (status.includes("reprovado")) statusClass = "reprovado";
    
            const podeSolicitarTroca = status.includes("entregue");
    
            tbody.innerHTML += `
                <tr>
                    <td>${dataFormatada}</td>
                    <td>${livro.nome}</td>
                    <td>R$ ${valor}</td>
                    <td>${venda.formaPagamento}</td>
                    <td>${endereco}</td>
                    <td class="status-cell"><span class="status-btn ${statusClass}">${venda.status}</span></td>
                    <td>
                        ${podeSolicitarTroca ? `<button class="status-btn troca" onclick="confirmarAcao(this, 'Em Troca')">Solicitar Troca</button>` : ""}
                    </td>
                </tr>
            `;
        });
    
        openModal('modalTabela');
    };    

    function checkZoomAndSize() {
        let table = document.getElementById("myTable");
        if (!table) return;

        let isZoomed = window.devicePixelRatio >= 1.75;
        let isSmallScreen = window.innerWidth <= 1100;

        table.classList.toggle("zoomed", isZoomed || isSmallScreen);
    }
});
