document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const livroId = urlParams.get("livroId");
    const quantidade = urlParams.get("quantidade") || 1;

    document.getElementById("cadastroEnderecoForm").addEventListener("submit", async function (event) {
        event.preventDefault(); // Impede o envio tradicional do formulário

        // Capturando valores do formulário
        let tipoLogradouro = document.querySelectorAll("input")[0].value;
        let logradouro = document.querySelectorAll("input")[1].value;
        let numero = document.querySelectorAll("input")[2].value;
        let bairro = document.querySelectorAll("input")[3].value;
        let cep = document.querySelectorAll("input")[4].value;
        let cidade = document.querySelectorAll("input")[5].value;
        let estado = document.querySelectorAll("input")[6].value;
        let pais = document.querySelectorAll("input")[7].value;
        let observacoes = document.querySelector("textarea").value;

        // Capturando opções de endereço
        let residencia = document.getElementById("residencia").value === "sim";
        let entrega = document.getElementById("entrega").value === "sim";
        let cobranca = document.getElementById("cobranca").value === "sim";

        // Verifica se pelo menos um dos tipos foi marcado
        if (!residencia && !entrega && !cobranca) {
            abrirModal(); // Exibe o modal de erro
            return;
        }

        // Criando objeto EnderecoDTO
        let enderecoDTO = {
            tipo: tipoLogradouro,
            logradouro: logradouro,
            numero: numero,
            bairro: bairro,
            cep: cep,
            cidade: cidade,
            estado: estado,
            pais: pais,
            observacoes: observacoes,
            residencial: residencia,
            entrega: entrega,
            cobranca: cobranca
        };

        try {
            // Enviando os dados via fetch para o backend
            const response = await fetch(`/clientes/me/enderecos/add`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(enderecoDTO)
            });

            if (response.ok) {
                alert("Endereço cadastrado com sucesso!");

                // Redirecionamento baseado nos parâmetros
                if (livroId) {
                    window.location.href = `/pagamento?livroId=${livroId}&quantidade=${quantidade}`;
                } else {
                    window.location.href = `/pagar-carrinho`;
                }
            } else {
                const errorData = await response.json();
                alert(errorData.erro || "Erro ao cadastrar endereço.");
            }
        } catch (error) {
            alert("Erro inesperado ao cadastrar endereço.");
            console.error("Erro:", error);
        }
    });
});

function abrirModal() {
    document.getElementById("errorModal").style.display = "flex";
}

function fecharModal() {
    document.getElementById("errorModal").style.display = "none";
}

function cancelarCadastro() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back(); // Volta para a página anterior
    }
}
 // Função para buscar e exibir o nome do cliente
async function carregarNomeCliente() {

    try {
        // Requisição ao backend para buscar os dados do cliente
        const response = await fetch(`/clientes/me`);
        
        if (!response.ok) {
            throw new Error("Erro ao buscar o cliente.");
        }

        // Converte resposta para JSON
        const cliente = await response.json();

        // Atualiza o título da página com o nome do cliente
        document.getElementById("tituloEndereco").textContent = `Novo Endereço para ${cliente.nome}`;
    } catch (error) {
        console.error("Erro ao carregar o nome do cliente:", error);
    }
}

// Chama a função ao carregar a página
carregarNomeCliente();


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