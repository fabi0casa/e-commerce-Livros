document.getElementById("cadastroClienteForm").addEventListener("submit", function(event) {
    var senha = document.getElementById("senha").value;
    var confirmarSenha = document.getElementById("confirmarSenha").value;

    if (senha !== confirmarSenha) {
        event.preventDefault(); // Impede o envio do formulário
        document.getElementById("senhaModal").style.display = "flex"; // Exibe o modal
    }
});

function fecharModalSenha() {
    document.getElementById("senhaModal").style.display = "none"; // Fecha o modal
}

function cancelarCadastro() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back(); // Volta para a página anterior
    }
}

function togglePassword(inputId, icon) {
    var input = document.getElementById(inputId);
    var img = icon.querySelector("img"); // Seleciona a imagem dentro do span

    if (input.type === "password") {
        input.type = "text";
        img.src = "/img/olhofechado.png/"; // Ícone de "olho fechado"
    } else {
        input.type = "password";
        img.src = "/img/olhoaberto.png/"; // Ícone de "olho aberto"
    }
}