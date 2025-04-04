document.addEventListener("DOMContentLoaded", function() {
    const contaPedidos = `
            <div id="pedidos" class="secao">
                <h2>Meus Pedidos</h2>
                <div class="pedido-list">
                    <div class="pedido-item">
                        <div class="pedido-info">
                            <div class="pedido-name"> Pedido Nº 202503011239812789</div>
                            <div class="pedido-details">01/03/2025 - 3 produtos</div>
                        </div>
                        <div class="pedido-actions">
                            <button onclick="openModal('modalTabela')">Ver</button>
                        </div>
                    </div>

                    <div class="pedido-item">
                        <div class="pedido-info">
                            <div class="pedido-name"> Pedido Nº 202503017896542357</div>
                            <div class="pedido-details">01/03/2025 - 5 produtos</div>
                        </div>
                        <div class="pedido-actions">
                            <button onclick="openModal('modalTabela')">Ver</button>
                        </div>
                    </div>

                    <div class="pedido-item">
                        <div class="pedido-info">
                            <div class="pedido-name"> Pedido Nº 202503014872450989</div>
                            <div class="pedido-details">01/03/2025 - 1 produtos</div>
                        </div>
                        <div class="pedido-actions">
                            <button onclick="openModal('modalTabela')">Ver</button>
                        </div>
                    </div>
                </div>

            </div>
             <!-- Modal de amostragem da tabela-->
            <div id="modalTabela" class="modal-tabela">
                <div class="modal-tabela-content">
                <span class="close" onclick="closeModal('modalTabela')">&times;</span>
                <h2>Pedido Nº 202503011239812789</h2>
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
                    <tbody>
                        <tr>
                            <td>2025-02-21</td>
                            <td>O Senhor dos Anéis</td>
                            <td>R$ 150,00</td>
                            <td>Cartão de Crédito</td>
                            <td>Rua Exemplo, 123 - São Paulo, SP</td>
                            <td class="status-cell"><span class="status-btn aprovado">Aprovado</span></td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                            <td>2025-02-20</td>
                            <td>1984</td>
                            <td>R$ 250,00</td>
                            <td>Cupom</td>
                            <td>Av. Teste, 456 - Rio de Janeiro RJ</td>
                            <td class="status-cell"><span class="status-btn transito">Em Transporte</span></td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                            <td>2025-02-19</td>
                            <td>Dom Quixote</td>
                            <td>R$ 75,00</td>
                            <td>Cartão de Crédito</td>
                            <td>Av. Teste, 458 - Rio de Janeiro RJ</td>
                            <td class="status-cell"><span class="status-btn entregue">Entregue</span></td>
                            <td>
                                <button class="status-btn troca" onclick="confirmarAcao(this, 'Em Troca')">Solicitar Troca</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </div>
            </div>
            <!-- Modal de Confirmação -->
            <div id="modalConfirmacao" class="modal">
                <div class="modal-content">
                    <h2>Confirmar Ação</h2>
                    <p>Deseja realmente solicitar a Troca desse produto?</p>
                    <div id="checkboxContainer" style="display: none;">
                    </div>
                    </br>
                    <button id="confirmarBtn">Confirmar</button>
                    <button onclick="fecharModal()">Cancelar</button>
                </div>
            </div>
    `;

    document.getElementById("contaPedidos").innerHTML = contaPedidos;

    document.getElementById("confirmarBtn").addEventListener("click", function() {
        alterarStatus(botaoClicado, novoStatus);
        fecharModal();
    });

    function checkZoomAndSize() {
        let table = document.getElementById("myTable");
        
        // Condições para reduzir a fonte
        let isZoomed = window.devicePixelRatio >= 1.75;
        let isSmallScreen = window.innerWidth <= 1100; // Ajuste conforme necessário

        if (isZoomed || isSmallScreen) {
            table.classList.add("zoomed");
        } else {
            table.classList.remove("zoomed");
        }
    }

    // Checa ao carregar e quando a tela redimensiona
    window.addEventListener("load", checkZoomAndSize);
    window.addEventListener("resize", checkZoomAndSize);
    
});


