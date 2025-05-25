document.getElementById("alterarSenhaForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Impede o envio do formulário

    const clienteId = new URLSearchParams(window.location.search).get("clienteId");
    const senhaAtual = document.getElementById("senhaAtual").value;
    const novaSenha = document.getElementById("senha").value;
    const confirmarNovaSenha = document.getElementById("confirmarSenha").value;

    if (!clienteId) {
        alert("Erro: ID do cliente não encontrado.");
        return;
    }

    // Correção: usar novaSenha e confirmarNovaSenha na verificação
    if (novaSenha !== confirmarNovaSenha) {
        document.getElementById("senhaModal").style.display = "flex"; // Exibe o modal
        return;
    }

    const dadosSenha = {
        id: clienteId,
        senhaAtual: senhaAtual,
        novaSenha: novaSenha
    };

    fetch("/clientes/alterarSenha", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dadosSenha)
    })
    .then(response => response.json())
    .then(data => {
        if (data.erro) {
            alert("Erro ao alterar senha: " + data.erro);
        } else {
            alert("Senha alterada com sucesso!");
            window.location.href = "/administrador/gerenciar-clientes/gerenciarClientes";
        }
    })
    .catch(error => {
        console.error("Erro ao alterar senha:", error);
        alert("Erro ao alterar senha. Tente novamente.");
    });
});

function fecharModalSenha() {
    document.getElementById("senhaModal").style.display = "none"; // Fecha o modal
}

function cancelarAlteracaoSenha() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back(); // Volta para a página anterior
    }
}

function togglePassword(inputId, icon) {
    var input = document.getElementById(inputId);
    var img = icon.querySelector("img"); // Seleciona a imagem dentro do span

    if (input.type === "password") {
        input.type = "text";
        img.src = "/img//olhofechado.png"; // Ícone de "olho fechado"
    } else {
        input.type = "password";
        img.src = "/img//olhoaberto.png"; // Ícone de "olho aberto"
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const clienteId = new URLSearchParams(window.location.search).get("clienteId");

    if (!clienteId) {
        document.getElementById("clienteNome").innerText = "ID não encontrado";
        return;
    }

    fetch(`/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            document.getElementById("clienteNome").innerText = cliente.nome;
        })
        .catch(error => {
            console.error("Erro ao buscar cliente:", error);
            document.getElementById("clienteNome").innerText = "Erro ao carregar";
        });
});