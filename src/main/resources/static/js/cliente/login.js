async function carregarClientes() {
    try {
        let response = await fetch('/clientes/all');
        let clientes = await response.json();

        let clientList = document.getElementById("clientList");
        clientList.innerHTML = ""; // Limpa a lista

        if (clientes.length === 0) {
            clientList.innerHTML = "<p>Nenhum cliente encontrado.</p>";
            return;
        }

        clientes.forEach(cliente => {
            let div = document.createElement("div");
            div.classList.add("client-item");

            div.innerHTML = `
                <div class="client-info">
                    <div class="client-name">${cliente.nome}</div>
                    <div class="client-details">
                        ${cliente.email} - ${cliente.telefone} - ${formatarData(cliente.dataNascimento)}
                        ${cliente.admin ? '<span class="isAdmin">✪ Administrador</span>' : ''}
                    </div>
                </div>
                <div class="client-actions">
                    <button onclick="logarComo(${cliente.id})">Logar Como</button>
                </div>
            `;

            clientList.appendChild(div);
        });
    } catch (error) {
        console.error("Erro ao carregar clientes:", error);
    }
}

async function logarComo(clienteId) {
    try {
        let response = await fetch('/sessao/logar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ clienteId: clienteId })
        });

        if (response.ok) {
            const clienteLogado = await response.json();
            if (clienteLogado.isAdmin) {
                window.location.href = "/dashboard";
            } else {
                window.location.href = "/home";
            }
        } else {
            console.error("Erro ao logar como cliente.");
        }
    } catch (error) {
        console.error("Erro na requisição de login:", error);
    }
}

function formatarData(data) {
    if (!data) return "";
    let partes = data.split("-");
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

// Carregar os clientes ao carregar a página
carregarClientes();