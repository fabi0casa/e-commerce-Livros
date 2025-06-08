document.addEventListener("DOMContentLoaded", function() {
    const contaCartao = `
             <div id="cartao" class="secao">
                <h2>Cadastrar Novo Cartão</h2>
                <form id="cadastroCartaoForm" class="formulario">
                    <label>Número do Cartão:</label>
                    <input type="text" id="numeroCartao" required>
        
                    <label>Nome Impresso:</label>
                    <input type="text" id="nomeImpresso" required>
        
                    <label>Bandeira:</label>
                    <select id="bandeira" required>
                        <option value="">Selecione</option>
                        <option value="Visa">Visa</option>
                        <option value="Elo">Elo</option>
                        <option value="MasterCard">Mastercard</option>
                    </select>
        
                    <label>Código de segurança:</label>
                    <input type="text" id="codigoSeguranca" required>
        
                    <label>Tornar esse cartão preferencial?</label>
                    <select id="preferencial" required>
                        <option value="">Selecione</option>
                        <option value="sim">Sim</option>
                        <option value="nao">Não</option>
                    </select>
                    <button type="submit">Salvar Cartão</button>
                </form>
            </div>
    `;

    document.getElementById("contaCartao").innerHTML = contaCartao;

    fetch("/bandeiras/all")
    .then(response => response.json())
    .then(bandeiras => {
        const selectBandeira = document.getElementById("bandeira");
        selectBandeira.innerHTML = `<option value="">Selecione</option>`;
        bandeiras.forEach(bandeira => {
            selectBandeira.innerHTML += `<option value="${bandeira.id}">${bandeira.nome}</option>`;
        });
    })
    .catch(error => {
        console.error("Erro ao carregar bandeiras:", error);
        document.getElementById("bandeira").innerHTML = `<option value="">Erro ao carregar</option>`;
    });

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

    document.getElementById("cadastroCartaoForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const cartaoData = {
            numeroCartao: document.getElementById("numeroCartao").value,
            nomeImpresso: document.getElementById("nomeImpresso").value,
            bandeiraId: document.getElementById("bandeira").value,
            codigoSeguranca: document.getElementById("codigoSeguranca").value,
            preferencial: document.getElementById("preferencial").value === "sim"
        };

        fetch("/cartoes/me/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cartaoData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.erro) {
                    alert("Erro ao cadastrar: " + data.erro);
                } else {
                    alert("Cartão cadastrado com sucesso!");
                    window.location.href = `/conta`;
                }
            })
            .catch(error => {
                console.error("Erro ao enviar formulário:", error);
                alert("Erro ao enviar o formulário. Tente novamente mais tarde.");
            });
    });
});
