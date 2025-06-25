// novoAdmin.js

function cancelarCadastro() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("adminForm");

    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Impede envio padrão do formulário

        const senha = document.getElementById("senha").value;
        const confirmarSenha = document.getElementById("confirmarSenha").value;

        if (senha !== confirmarSenha) {
            document.getElementById("senhaModal").style.display = "flex";
            return;
        }

        const loader = document.getElementById("loader");
        loader.style.display = "flex";

        // Captura os dados do formulário
        const adminData = {
            nome: document.getElementById("nome").value,
            dataNascimento: document.getElementById("dataNascimento").value,
            cpf: document.getElementById("cpf").value,
            genero: document.getElementById("genero").value,
            telefone: document.getElementById("telefone").value,
            email: document.getElementById("email").value,
            senha: senha,
            ranking: 0,
            admin: true
        };

        fetch("/clientes/add/admin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(adminData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.erro) {
                alert("Erro ao cadastrar administrador: " + data.erro);
            } else {
                alert("Administrador cadastrado com sucesso!");
                window.location.href = "/gerenciar-clientes";
            }
        })
        .catch(error => {
            console.error("Erro ao cadastrar admin:", error);
            alert("Erro ao cadastrar administrador. Tente novamente.");
        })
        .finally(() => {
            loader.style.display = "none";
        });
    });
});

function fecharModalSenha() {
    document.getElementById("senhaModal").style.display = "none";
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
