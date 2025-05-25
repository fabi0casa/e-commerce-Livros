document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const clienteId = params.get("clienteId");

    if (!clienteId) {
        alert("Erro: ID do cliente não encontrado.");
        return;
    }

    // Buscar nome do cliente
    fetch(`/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            document.getElementById("clienteNome").textContent = cliente.nome;
        })
        .catch(error => console.error("Erro ao carregar nome do cliente:", error));

    // Buscar transações do cliente
    fetch(`/transacoes/cliente/${clienteId}`)
        .then(response => response.json())
        .then(data => {
            if (!data || data.length === 0) {
                document.getElementById("transacoesBody").innerHTML = "<tr><td colspan='6'>Nenhuma transação encontrada.</td></tr>";
                return;
            }

            let tabelaHTML = "";
            data.forEach(transacao => {
                tabelaHTML += `
                    <tr>
                        <td>${transacao.dataHora}</td>
                        <td>${transacao.tipoOperacao}</td>
                        <td>${transacao.produto}</td>
                        <td>${transacao.quantidade}</td>
                        <td>R$ ${transacao.valor.toFixed(2)}</td>
                        <td>${transacao.formaPagamento}</td>
                    </tr>
                `;
            });

            document.getElementById("transacoesBody").innerHTML = tabelaHTML;
        })
        .catch(error => console.error("Erro ao carregar transações:", error));
});