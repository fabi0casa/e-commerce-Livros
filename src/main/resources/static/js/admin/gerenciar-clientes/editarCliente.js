function cancelarEdicao() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back(); // Volta para a página anterior
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // Função para extrair o clienteId da URL
    function getClienteIdFromUrl() {
        const params = new URLSearchParams(window.location.search);
        return params.get("clienteId");
    }

    const clienteId = getClienteIdFromUrl();
    if (clienteId) {
        fetch(`/clientes/${clienteId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Erro ao buscar dados do cliente");
                }
                return response.json();
            })
            .then(cliente => {
                // Preencher os campos do formulário
                document.getElementById("nome").value = cliente.nome || "";
                document.getElementById("dataNascimento").value = cliente.dataNascimento || "";
                document.getElementById("cpf").value = cliente.cpf || "";
                document.getElementById("telefone").value = cliente.telefone || "";
                document.getElementById("email").value = cliente.email || "";

                // Atualizar título com o nome do cliente
                document.querySelector("h2").textContent = `Edição do Cadastro de ${cliente.nome}`;
                
                // Preencher o select de gênero
                if (cliente.genero) {
                    const selectGenero = document.getElementById("genero");
                    selectGenero.value = cliente.genero.toLowerCase();
                }
            })
            .catch(error => {
                console.error("Erro:", error);
                alert("Erro ao carregar os dados do cliente.");
            });
    }
});

document.getElementById("edicaoClienteForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Capturar os valores dos inputs
    const clienteId = new URLSearchParams(window.location.search).get("clienteId");
    const nome = document.getElementById("nome").value;
    const dataNascimento = document.getElementById("dataNascimento").value;
    const cpf = document.getElementById("cpf").value;
    const genero = document.getElementById("genero").value;
    const telefone = document.getElementById("telefone").value;
    const email = document.getElementById("email").value;

    if (!clienteId) {
        alert("Erro: ID do cliente não encontrado.");
        return;
    }

    // Criar objeto DTO para enviar
    const clienteDTO = {
        id: clienteId,
        nome: nome,
        dataNascimento: dataNascimento,
        cpf: cpf,
        genero: genero,
        telefone: telefone,
        email: email
    };

    // Enviar requisição PUT para atualizar cliente
    fetch("/clientes/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(clienteDTO)
    })
    .then(response => response.json())
    .then(data => {
        if (data.erro) {
            alert("Erro ao atualizar: " + data.erro);
        } else {
            alert("Cliente atualizado com sucesso!");
            window.location.href = "/gerenciar-clientes"; // Redireciona após sucesso
        }
    })
    .catch(error => {
        console.error("Erro ao atualizar cliente:", error);
        alert("Erro ao atualizar cliente. Tente novamente.");
    });
});