document.addEventListener("DOMContentLoaded", function() {
    const contaPedidos = `
            <div id="pedidos" class="secao">
                <h2>Meus Pedidos</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Data</th>
                            <th>Livro</th>
                            <th>Quantidade</th>
                            <th>Valor</th>
                            <th>Forma de Pagamento</th>
                            <th>Status da Compra</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>2025-02-21</td>
                            <td>O Senhor dos Anéis</td>
                            <td>1</td>
                            <td>R$ 150,00</td>
                            <td>Cartão de Crédito</td>
                            <td class="status-cell"><span class="status-btn aprovado">Aprovado</span></td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                            <td>2025-02-20</td>
                            <td>1984</td>
                            <td>2</td>
                            <td>R$ 250,00</td>
                            <td>Cupom</td>
                            <td class="status-cell"><span class="status-btn transito">Em Transporte</span></td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                            <td>2025-02-19</td>
                            <td>Dom Quixote</td>
                            <td>1</td>
                            <td>R$ 75,00</td>
                            <td>Cartão de Crédito</td>
                            <td class="status-cell"><span class="status-btn entregue">Entregue</span></td>
                            <td>
                                <button class="status-btn troca" onclick="confirmarAcao(this, 'Em Troca')">Solicitar Troca</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
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
    
});


