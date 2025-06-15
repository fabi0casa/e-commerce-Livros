document.addEventListener("DOMContentLoaded", async () => {
    const contaEndereco = `
      <div id="endereco" class="secao">
        <form id="cadastroEnderecoForm" class="formulario">
            <h2>Cadastrar Novo Endereço</h2>
            <label>Tipo de Logradouro:</label>
            <input type="text" id="tipoLogradouro" required placeholder="Ex: Rua, Avenida...">
            
            <label>Logradouro:</label>
            <input type="text" id="logradouro" required>
            
            <label>Número:</label>
            <input type="text" id="numero" required>
            
            <label>Bairro:</label>
            <input type="text" id="bairro" required>
            
            <label>CEP:</label>
            <input type="text" id="cep" required placeholder="00000-000">
            
            <label>Cidade:</label>
            <input type="text" id="cidade" required>
            
            <label>Estado:</label>
            <input type="text" id="estado" required>
            
            <label>País:</label>
            <input type="text" id="pais" required value="Brasil">
            
            <label>Observações:</label>
            <textarea id="observacoes"></textarea>

            <h3>Tipo de Endereço</h3>
            <label>Usar como Residencial?</label>
            <select id="residencia" name="residencial" required>
                <option value="">Selecione</option>
                <option value="sim">Sim</option>
                <option value="nao">Não</option>
            </select>

            <label>Usar como Entrega?</label>
            <select id="entrega" name="entrega" required>
                <option value="">Selecione</option>
                <option value="sim">Sim</option>
                <option value="nao">Não</option>
            </select>

            <label>Usar como Cobrança?</label>
            <select id="cobranca" name="cobranca" required>
                <option value="">Selecione</option>
                <option value="sim">Sim</option>
                <option value="nao">Não</option>
            </select>

            <small>*Selecione pelo menos uma das opções acima como "Sim".</small>
            <br><br>
            <button type="submit">Salvar Endereço</button>
        </form>
        </div>
        <!-- Modal de Erro -->
        <div id="errorModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('errorModal')">&times;</span>
                <h2>Erro!</h2>
                <p>Você deve selecionar "Sim" em pelo menos um tipo de endereço.</p>
                <button onclick="closeModal('errorModal')">OK</button>
            </div>
        </div>
    `;

    document.getElementById("contaEndereco").innerHTML = contaEndereco;

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

    let formEndereco = document.getElementById("endereco");
        if (formEndereco) {
            formEndereco.addEventListener("submit", function (event) {
                let residencia = document.getElementById("residencia").value;
                let entrega = document.getElementById("entrega").value;
                let cobranca = document.getElementById("cobranca").value;

                if (!(residencia === "sim" || entrega === "sim" || cobranca === "sim")) {
                    event.preventDefault();
                    openModal('errorModal'); 
                }
            });
        }

    document.getElementById("cadastroEnderecoForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    // Capturando valores do formulário
    let tipoLogradouro = document.getElementById("tipoLogradouro");
    let logradouro = document.getElementById("logradouro");
    let numero = document.getElementById("numero");
    let bairro = document.getElementById("bairro");
    let cep = document.getElementById("cep");
    let cidade = document.getElementById("cidade");
    let estado = document.getElementById("estado");
    let pais = document.getElementById("pais");
    let observacoes = document.getElementById("observacoes");

    // Capturando opções de endereço
    let residencia = document.getElementById("residencia").value === "sim";
    let entrega = document.getElementById("entrega").value === "sim";
    let cobranca = document.getElementById("cobranca").value === "sim";

    // Verifica se pelo menos um dos tipos foi marcado
    if (!residencia && !entrega && !cobranca) {
        abrirModal(); // Exibe o modal de erro
        return;
    }

    let enderecoDTO = {
        tipo: tipoLogradouro.value,
        logradouro: logradouro.value,
        numero: numero.value,
        bairro: bairro.value,
        cep: cep.value,
        cidade: cidade.value,
        estado: estado.value,
        pais: pais.value,
        observacoes: observacoes.value,
        residencial: residencia,
        entrega: entrega,
        cobranca: cobranca
    };

    // Validação: pelo menos uma opção marcada como sim
    if (!enderecoDTO.residencial && !enderecoDTO.entrega && !enderecoDTO.cobranca) {
        openModal("errorModal");
        return;
    }

    try {
        const response = await fetch(`/clientes/me/enderecos/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(enderecoDTO)
        });

        if (response.ok) {
            alert("Endereço cadastrado com sucesso!");
            window.location.href = `/conta`;
        } else {
            const error = await response.json();
            alert(error.erro || "Erro ao cadastrar endereço.");
        }
    } catch (e) {
        console.error("Erro inesperado:", e);
        alert("Erro inesperado ao cadastrar endereço.");
    }
    loader.style.display = "none";

    });

    function abrirModal() {
    document.getElementById("errorModal").style.display = "flex";
    }

    function fecharModal() {
    document.getElementById("errorModal").style.display = "none";
    }

});
