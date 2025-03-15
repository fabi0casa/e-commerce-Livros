document.addEventListener("DOMContentLoaded", function () {
    const nomeInput = document.getElementById("nome");
    const cpfInput = document.getElementById("cpf");
    const telefoneInput = document.getElementById("telefone");

    // Nome: impede números e caracteres especiais, permite apenas letras e espaços
    nomeInput.addEventListener("keypress", function (event) {
        if (!/^[a-zA-ZÀ-ÿ\s]+$/.test(event.key)) {
            event.preventDefault();
        }
    });

    // CPF: impede qualquer entrada fora do formato correto e formata dinamicamente
    cpfInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, ""); // Remove tudo que não for número
        if (value.length > 11) value = value.slice(0, 11); // Limita a 11 caracteres numéricos

        // Aplica a máscara de CPF (000.000.000-00)
        let formattedValue = value;
        if (value.length > 9) {
            formattedValue = `${value.slice(0, 3)}.${value.slice(3, 6)}.${value.slice(6, 9)}-${value.slice(9, 11)}`;
        } else if (value.length > 6) {
            formattedValue = `${value.slice(0, 3)}.${value.slice(3, 6)}.${value.slice(6, 9)}`;
        } else if (value.length > 3) {
            formattedValue = `${value.slice(0, 3)}.${value.slice(3, 6)}`;
        }
        this.value = formattedValue;
    });

    // Telefone: impede qualquer entrada fora do formato correto e formata dinamicamente
    telefoneInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, ""); // Remove tudo que não for número
        if (value.length > 11) value = value.slice(0, 11); // Limita a 11 caracteres numéricos

        // Aplica a máscara de telefone (XX) XXXXX-XXXX
        let formattedValue = value;
        if (value.length > 6) {
            formattedValue = `(${value.slice(0, 2)}) ${value.slice(2, 7)}-${value.slice(7, 11)}`;
        } else if (value.length > 2) {
            formattedValue = `(${value.slice(0, 2)}) ${value.slice(2, 7)}`;
        } else if (value.length > 0) {
            formattedValue = `(${value}`;
        }
        this.value = formattedValue;
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const senhaInput = document.getElementById("senha");
    const senhaFeedback = document.getElementById("senha-feedback");

    senhaInput.addEventListener("input", function () {
        const senha = senhaInput.value;
        const senhaValida = validarSenha(senha);

        if (senhaValida) {
            senhaFeedback.textContent = "";
            senhaInput.style.borderColor = "green"; // Borda verde se válida
        } else {
            senhaFeedback.innerHTML = "A senha deve ter:<br> - Pelo menos 8 caracteres<br> - Letras maiúsculas e minúsculas<br> - Pelo menos um caractere especial.";
            senhaInput.style.borderColor = "red"; // Borda vermelha se inválida
        }
    });
});

// Função de validação da senha
function validarSenha(senha) {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$/;
    return regex.test(senha);
}

document.addEventListener("DOMContentLoaded", function () {
    const tipoLogradouroInput = document.getElementById("tipoLogradouro");
    const logradouroInput = document.getElementById("logradouro");
    const numeroInput = document.getElementById("numero");
    const bairroInput = document.getElementById("bairro");
    const cepInput = document.getElementById("cep");
    const cidadeInput = document.getElementById("cidade");
    const estadoInput = document.getElementById("estado");
    const paisInput = document.getElementById("pais");

    // Permite apenas letras e espaços
    function validarTexto(input) {
        input.addEventListener("keypress", function (event) {
            if (!/^[a-zA-ZÀ-ÿ\s]+$/.test(event.key)) {
                event.preventDefault();
            }
        });
    }

    // Aplica a validação de texto nos campos que só aceitam letras
    [tipoLogradouroInput, logradouroInput, bairroInput, cidadeInput, estadoInput, paisInput].forEach(validarTexto);

    // Permite apenas números no campo "Número"
    numeroInput.addEventListener("keypress", function (event) {
        if (!/[0-9]/.test(event.key)) {
            event.preventDefault();
        }
    });

    // CEP: só números e formatação automática (00000-000)
    cepInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, ""); // Remove tudo que não for número
        if (value.length > 8) value = value.slice(0, 8); // Limita a 8 caracteres numéricos

        let formattedValue = "";
        if (value.length > 5) {
            formattedValue = `${value.slice(0, 5)}-${value.slice(5, 8)}`;
        } else {
            formattedValue = value;
        }
        this.value = formattedValue;
    });
});

document.getElementById("cadastroClienteForm").addEventListener("submit", function (event) {
    event.preventDefault(); 
    var senha = document.getElementById("senha").value;
    var confirmarSenha = document.getElementById("confirmarSenha").value;

    // Verifica se as senhas são iguais
    if (senha !== confirmarSenha) {
        event.preventDefault(); // Impede o envio do formulário
        document.getElementById("senhaModal").style.display = "flex"; // Exibe o modal de erro
        return; // Impede o envio do formulário para execução posterior
    }

    const formData = new FormData(this);

    fetch("/clientes/add", {
        method: "POST",
        body: formData
    })
    .then(response => response.json()) // Converte a resposta para JSON
    .then(data => {
        if (data.erro) {
            alert("Erro ao cadastrar: " + data.erro); // Exibe a mensagem de erro
        } else {
            alert("Sucesso! " + data.mensagem); // Exibe a mensagem de sucesso
            window.location.href = "/administrador/gerenciar-clientes/gerenciarClientes"; // Redireciona após sucesso
        }
    })
    .catch(error => {
        console.error("Erro ao enviar formulário:", error);
        alert("Erro ao enviar o formulário. Tente novamente mais tarde.");
    });
});


