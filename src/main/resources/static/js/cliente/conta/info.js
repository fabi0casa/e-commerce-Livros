document.addEventListener("DOMContentLoaded", function() {
    fetch(`/clientes/me`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar dados do cliente.');
            }
            return response.json();
        })
        .then(cliente => {
            const contaInfo = `
                <div id="info" class="secao ativa">
                    <h2>Minha Conta</h2>
                    <div class="info-container">
                        <div class="info-avatar">
                            ${gerarAvatarCliente(cliente.nome)}
                        </div>
                        <div class="info-text">
                            <p><strong>Nome:</strong> ${cliente.nome}</p>
                            <p><strong>Email:</strong> ${cliente.email}</p>
                            <p><strong>Telefone:</strong> ${cliente.telefone}</p>
                            <p><strong>Data de Nascimento:</strong> ${formatarData(cliente.dataNascimento)}</p>
                            <p><strong>Gênero:</strong> ${cliente.genero}</p>
                            <p><strong>CPF:</strong> ${cliente.cpf}</p>
                        </div>
                        <div class="info-buttons">
                            <button onclick="openModal('addressModal')">Ver Endereços</button>
                            <button onclick="openModal('cardModal')">Ver Cartões</button>
                            <button onclick="openModal('cupomModal')">Ver Cupons</button>
                        </div>
                    </div>
                </div>
            
                ${gerarModalEnderecos(cliente.enderecos)}
                ${gerarModalCartoes(cliente.cartoes)}
                ${gerarModalCupons(cliente.cupons)}
            `;
        

            document.getElementById("contaInfo").innerHTML = contaInfo;

            // Ativa os accordions depois que o HTML foi carregado
            setupAccordions();
        })
        .catch(error => {
            console.error('Erro:', error);
        });
});

function formatarData(data) {
    // Assume formato "YYYY-MM-DD"
    if (!data) return '';
    const partes = data.split("-");
    return `${partes[2]}/${partes[1]}/${partes[0]}`; // dia/mês/ano
}

function gerarModalEnderecos(enderecos) {
    let html = `
        <div id="addressModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('addressModal')">&times;</span>
                <h2>Endereços Cadastrados</h2>
    `;

    if (enderecos && enderecos.length > 0) {
        enderecos.forEach(endereco => {
            html += `
                <button class="accordion">${endereco.fraseIdentificadora}</button>
                <div class="panel">
                    <p><strong>Tipo:</strong> ${endereco.tipo}</p>
                    <p><strong>Logradouro:</strong> ${endereco.logradouro}</p>
                    <p><strong>Número:</strong> ${endereco.numero}</p>
                    <p><strong>Bairro:</strong> ${endereco.bairro}</p>
                    <p><strong>CEP:</strong> ${endereco.cep}</p>
                    <p><strong>Cidade:</strong> ${endereco.cidade}</p>
                    <p><strong>Estado:</strong> ${endereco.estado}</p>
                    <p><strong>País:</strong> ${endereco.pais}</p>
                    <p><strong>Observação:</strong> ${endereco.observacoes}</p>
                </div>
            `;
        });
    } else {
        html += `<p>Nenhum endereço cadastrado.</p>`;
    }

    html += `
            </div>
        </div>
    `;

    return html;
}

function gerarModalCartoes(cartoes) {
    let html = `
        <div id="cardModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('cardModal')">&times;</span>
                <h2>Cartões Cadastrados</h2>
    `;

    if (cartoes && cartoes.length > 0) {
        cartoes.forEach(cartao => {
            const numeroFormatado = '**** **** **** ' + cartao.numeroCartao.slice(-4);
            html += `
                <button class="accordion">${numeroFormatado}</button>
                <div class="panel">
                    <p><strong>Nome Impresso:</strong> ${cartao.nomeImpresso}</p>
                    <p><strong>Bandeira:</strong> ${cartao.bandeira.nome}</p>
                    <p><strong>Código de Segurança:</strong> ${cartao.codigoSeguranca}</p>
                    <p><strong>Preferencial:</strong> ${cartao.preferencial ? 'Sim' : 'Não'}</p>
                </div>
            `;
        });
    } else {
        html += `<p>Nenhum cartão cadastrado.</p>`;
    }

    html += `
            </div>
        </div>
    `;

    return html;
}

function gerarModalCupons(cupons) {
    let html = `
        <div id="cupomModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('cupomModal')">&times;</span>
                <h2>Meus Cupons</h2>
    `;

    if (cupons && cupons.length > 0) {
        cupons.forEach(cupom => {
            html += `
                <button class="accordion">Cupom ${cupom.codigo}</button>
                <div class="panel">
                    <p><strong>Codigo do cupom:</strong> ${cupom.codigo}</p>
                    <p><strong>Tipo:</strong> ${cupom.tipo}</p>
                    <p><strong>Valor:</strong> R$ ${cupom.valor.toFixed(2)}</p>
                </div>
            `;
        });
    } else {
        html += `<p>Nenhum cupom disponível.</p>`;
    }

    html += `
            </div>
        </div>
    `;

    return html;
}

// Essa função ativa a abertura/fechamento dos accordions:
function setupAccordions() {
    const acc = document.getElementsByClassName("accordion");
    for (let i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function () {
            this.classList.toggle("active");
            const panel = this.nextElementSibling;

            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
                panel.style.paddingTop = "0";
                panel.style.paddingBottom = "0";
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
                panel.style.paddingTop = "10px";
                panel.style.paddingBottom = "10px";
            }
        });
    }
}

function gerarAvatarCliente(nome) {
    const primeiroNome = nome.split(" ")[0];
    const inicial = primeiroNome.charAt(0).toUpperCase();

    // Cor determinística
    const charCode = inicial.charCodeAt(0);
    const hue = (charCode * 15) % 360;
    const corVibrante = `hsl(${hue}, 100%, 45%)`;

    return `
        <div class="account-circle" style="background-color: ${corVibrante}; width: 80px; height: 80px; font-size: 32px;">
            ${inicial}
        </div>
    `;
}