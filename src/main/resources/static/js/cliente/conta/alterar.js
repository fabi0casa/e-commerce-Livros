document.addEventListener("DOMContentLoaded", function() {
    const contaAlterar = `
            <div id="alterar" class="secao">
                <h2>Alterar Dados</h2>
                <form id="edicaoClienteForm" class="formulario">
                    <label>Nome Completo:</label>
                    <input type="text" id="nomeCliente" required>

                    <label>Data de Nascimento:</label>
                    <input type="date" id="dataNascimento" required>

                    <label>CPF:</label>
                    <input type="text" id="cpf" required placeholder="000.000.000-00">

                    <label>Gênero:</label>
                    <select id="genero" required>
                        <option value="">Selecione</option>
                        <option value="masculino">Masculino</option>
                        <option value="feminino">Feminino</option>
                        <option value="outro">Outro</option>
                        <option value="nao_informar">Prefiro não informar</option>
                    </select>

                    <h3>Contato</h3>
                    <label>Telefone:</label>
                    <input type="text" id="telefone" required placeholder="Ex: 99999-9999">

                    <label>Email:</label>
                    <input type="email" id="email" required>
                    <button type="submit">Salvar Alterações</button>
                </form>
            </div>
    `;

    document.getElementById("contaAlterar").innerHTML = contaAlterar;

    fetch(`/clientes/me`)
    .then(response => {
        if (!response.ok) {
            throw new Error("Erro ao buscar dados do cliente");
        }
        return response.json();
    })
    .then(cliente => {
        // Preencher os campos do formulário
        document.getElementById("nomeCliente").value = cliente.nome || "";
        document.getElementById("dataNascimento").value = cliente.dataNascimento || "";
        document.getElementById("cpf").value = cliente.cpf || "";
        document.getElementById("telefone").value = cliente.telefone || "";
        document.getElementById("email").value = cliente.email || "";
        
        // Preencher o select de gênero
        if (cliente.genero) {
            const selectGenero = document.getElementById("genero");
            selectGenero.value = cliente.genero.toLowerCase();
        }
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao carregar os dados do cliente.");
    });

    const nomeInput = document.getElementById("nomeCliente");
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

    document.getElementById("edicaoClienteForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const loader = document.getElementById("loader");
        loader.style.display = "flex";

        const nome = document.getElementById("nomeCliente").value;
        const dataNascimento = document.getElementById("dataNascimento").value;
        const cpf = document.getElementById("cpf").value;
        const genero = document.getElementById("genero").value;
        const telefone = document.getElementById("telefone").value;
        const email = document.getElementById("email").value;
    
        // Criar objeto DTO para enviar
        const clienteDTO = {
            nome: nome,
            dataNascimento: dataNascimento,
            cpf: cpf,
            genero: genero,
            telefone: telefone,
            email: email
        };
    
        // Enviar requisição PUT para atualizar cliente
        fetch("/clientes/update/me", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(clienteDTO)
        })
        .then(response => response.json())
        .then(data => {
            if (data.erro) {
                alert("Erro ao atualizar: " + data.erro);
            } else {
                alert("Cliente atualizado com sucesso!");
                window.location.href = "/conta";
            }
        })
        .catch(error => {
            console.error("Erro ao atualizar cliente:", error);
            alert("Erro ao atualizar cliente. Tente novamente.");
        })        
        .finally(() => {
            loader.style.display = "none";
        });
    });
});
