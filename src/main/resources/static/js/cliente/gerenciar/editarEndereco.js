document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const enderecoId = urlParams.get("enderecoId");

    if (!enderecoId) {
        alert("Erro: Endereço não encontrado!");
        return;
    }

    try {
        const response = await fetch(`/enderecos/${enderecoId}`);
        if (!response.ok) throw new Error("Erro ao buscar endereço.");

        const endereco = await response.json();

        // Preenchendo os campos do formulário com os dados recebidos
        document.getElementById("tipoLogradouro").value = endereco.tipo;
        document.getElementById("logradouro").value = endereco.logradouro;
        document.getElementById("numero").value = endereco.numero;
        document.getElementById("bairro").value = endereco.bairro;
        document.getElementById("cep").value = endereco.cep;
        document.getElementById("cidade").value = endereco.cidade;
        document.getElementById("estado").value = endereco.estado;
        document.getElementById("pais").value = endereco.pais;
        document.getElementById("observacoes").value = endereco.observacoes || "";

        document.getElementById("residencia").value = endereco.residencial ? "sim" : "nao";
        document.getElementById("entrega").value = endereco.entrega ? "sim" : "nao";
        document.getElementById("cobranca").value = endereco.cobranca ? "sim" : "nao";

    } catch (error) {
        console.error("Erro ao carregar endereço:", error);
        alert("Erro ao carregar os dados do endereço.");
    }
});

document.getElementById("cadastroEnderecoForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Impede o envio tradicional do formulário
    
    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    const urlParams = new URLSearchParams(window.location.search);
    const enderecoId = urlParams.get("enderecoId");
    const clienteId = urlParams.get("clienteId");

    if (!enderecoId) {
        alert("Erro: Endereço não encontrado!");
        return;
    }

    if (!clienteId) {
        alert("Erro: Cliente não encontrado!");
        return;
    }

    // Capturando valores do formulário
    let tipoLogradouro = document.getElementById("tipoLogradouro").value;
    let logradouro = document.getElementById("logradouro").value;
    let numero = document.getElementById("numero").value;
    let bairro = document.getElementById("bairro").value;
    let cep = document.getElementById("cep").value;
    let cidade = document.getElementById("cidade").value;
    let estado = document.getElementById("estado").value;
    let pais = document.getElementById("pais").value;
    let observacoes = document.getElementById("observacoes").value;

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
        const response = await fetch(`/enderecos/${enderecoId}/update`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(enderecoDTO)
        });

        // Tratando a resposta do backend
        const result = await response.json();

        if (response.ok) {
            alert("Endereço atualizado com sucesso!");
            window.location.href = `/enderecos-cliente?clienteId=${clienteId}`;
        } else {
            alert("Erro ao atualizar endereço: " + result.erro);
        }
    } catch (error) {
        alert("Erro inesperado ao atualizar endereço.");
        console.error("Erro:", error);
    }
    loader.style.display = "none";
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