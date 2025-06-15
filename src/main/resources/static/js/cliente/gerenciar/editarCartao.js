document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const cartaoId = urlParams.get("cartaoId");
    const clienteId = urlParams.get("clienteId");

    if (!cartaoId || !clienteId) {
        alert("Erro: Cartão ou Cliente não encontrado!");
        return;
    }

    let cartao = null;

    try {
        // Busca o cartão primeiro
        const responseCartao = await fetch(`/cartoes/${cartaoId}`);
        if (!responseCartao.ok) throw new Error("Erro ao buscar cartão.");
        cartao = await responseCartao.json();
    } catch (error) {
        console.error("Erro ao buscar cartão:", error);
        alert("Erro ao carregar os dados do cartão.");
        return;
    }

    try {
        // Depois busca as bandeiras e preenche o <select>
        const responseBandeiras = await fetch("/bandeiras/all");
        const bandeiras = await responseBandeiras.json();

        const selectBandeira = document.getElementById("bandeira");
        selectBandeira.innerHTML = `<option value="">Selecione</option>`;

        bandeiras.forEach(bandeira => {
            const option = document.createElement("option");
            option.value = bandeira.id;
            option.textContent = bandeira.nome;

            // Verifica se essa é a bandeira atual do cartão
            if (cartao.bandeira && cartao.bandeira.id === bandeira.id) {
                option.selected = true;
            }

            selectBandeira.appendChild(option);
        });

        // Agora preenche o resto do formulário
        document.getElementById("numeroCartao").value = cartao.numeroCartao;
        document.getElementById("nomeImpresso").value = cartao.nomeImpresso;
        document.getElementById("codigoSeguranca").value = cartao.codigoSeguranca;
        document.getElementById("preferencial").value = cartao.preferencial ? "sim" : "nao";

    } catch (error) {
        console.error("Erro ao carregar bandeiras:", error);
        document.getElementById("bandeira").innerHTML = `<option value="">Erro ao carregar</option>`;
    }
});


document.getElementById("cadastroCartaoForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    const urlParams = new URLSearchParams(window.location.search);
    const cartaoId = urlParams.get("cartaoId");
    const clienteId = urlParams.get("clienteId");

    if (!cartaoId || !clienteId) {
        alert("Erro: Cartão ou Cliente não encontrado!");
        return;
    }

    const cartaoDTO = {
        numeroCartao: document.getElementById("numeroCartao").value,
        nomeImpresso: document.getElementById("nomeImpresso").value,
        bandeiraId: parseInt(document.getElementById("bandeira").value), // CORRETO
        codigoSeguranca: document.getElementById("codigoSeguranca").value,
        preferencial: document.getElementById("preferencial").value === "sim"
    };    

    try {
        const response = await fetch(`/cartoes/${cartaoId}/update`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cartaoDTO)
        });

        const result = await response.json();

        if (response.ok) {
            alert("Cartão atualizado com sucesso!");
            window.location.href = `/cartoes-cliente?clienteId=${clienteId}`;
        } else {
            alert("Erro ao atualizar cartão: " + result.erro);
        }
    } catch (error) {
        console.error("Erro ao atualizar cartão:", error);
        alert("Erro inesperado ao atualizar cartão.");
    }
    loader.style.display = "none";
});

document.addEventListener("DOMContentLoaded", function () {
    const numeroCartaoInput = document.getElementById("numeroCartao");
    const nomeImpressoInput = document.getElementById("nomeImpresso");
    const codigoSegurancaInput = document.getElementById("codigoSeguranca");
    const form = document.getElementById("cadastroCartaoForm");

    // Validação do número do cartão
    numeroCartaoInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, ""); // Remove tudo que não for número

        // Detecta o tipo de cartão (baseado nos primeiros dígitos)
        let amexRegex = /^3[47]/; // American Express começa com 34 ou 37
        let isAmex = amexRegex.test(value);

        // Define limite máximo de caracteres
        let maxLength = isAmex ? 15 : 16;
        if (value.length > maxLength) {
            value = value.slice(0, maxLength);
        }

        let formattedValue;
        if (isAmex) {
            // Formato para American Express: XXXX XXXXXX XXXXX
            formattedValue = value.replace(/(\d{4})(\d{0,6})?(\d{0,5})?/, function (match, g1, g2, g3) {
                return [g1, g2, g3].filter(Boolean).join(" ");
            });
        } else {
            // Formato padrão: XXXX XXXX XXXX XXXX
            formattedValue = value.replace(/(\d{4})/g, "$1 ").trim();
        }

        this.value = formattedValue;
    });


    // Validação do nome impresso (apenas letras e espaços)
    nomeImpressoInput.addEventListener("keypress", function (event) {
        if (!/^[a-zA-ZÀ-ÿ\s]+$/.test(event.key)) {
            event.preventDefault();
        }
    });

    // Validação do código de segurança (apenas números, 3 ou 4 dígitos)
    codigoSegurancaInput.addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, ""); // Remove tudo que não for número
        if (this.value.length > 4) {
            this.value = this.value.slice(0, 4);
        }
    });
});


function cancelarCadastro() {
    if (confirm("Tem certeza que deseja cancelar? As alterações não serão salvas.")) {
        window.history.back();
    }
}