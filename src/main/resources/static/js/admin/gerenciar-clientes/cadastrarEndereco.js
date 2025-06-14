document.getElementById("cadastroEnderecoForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Impede o envio tradicional do formulário

    // Obtendo o ID do cliente da URL
    const urlParams = new URLSearchParams(window.location.search);
    const clienteId = urlParams.get("clienteId");

    if (!clienteId) {
        alert("Erro: ID do cliente não encontrado!");
        return;
    }

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
        const response = await fetch(`/clientes/${clienteId}/enderecos/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(enderecoDTO)
        });

        // Tratando a resposta do backend
        if (response.ok) {
            alert("Endereço cadastrado com sucesso!");
            window.location.href = `/enderecos-cliente?clienteId=${clienteId}`;
        } else {
            const errorData = await response.json();
            alert(errorData.erro || "Erro ao cadastrar endereço.");
        }
    } catch (error) {
        alert("Erro inesperado ao cadastrar endereço.");
        console.error("Erro:", error);
    }
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
    // Obtém o clienteId da URL
    const urlParams = new URLSearchParams(window.location.search);
    const clienteId = urlParams.get("clienteId");

    if (!clienteId) {
        console.error("Erro: ID do cliente não encontrado na URL.");
        return;
    }

    try {
        // Requisição ao backend para buscar apenas o nome do cliente
        const response = await fetch(`/clientes/${clienteId}/nome`);
        
        if (!response.ok) {
            throw new Error("Erro ao buscar o nome do cliente.");
        }

        // Converte resposta para texto
        const nome = await response.text();

        // Atualiza o título da página com o nome do cliente
        document.getElementById("tituloEndereco").textContent = `Novo Endereço para ${nome}`;
    } catch (error) {
        console.error("Erro ao carregar o nome do cliente:", error);
    }
}

// Chama a função ao carregar a página
carregarNomeCliente();