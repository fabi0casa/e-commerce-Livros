document.addEventListener("DOMContentLoaded", function() {
    const contaInfo = `
            <div id="info" class="secao ativa">
                <h2>Minha Conta</h2>
                <p><strong>Nome:</strong> João Silva</p>
                <p><strong>Email:</strong> joao@email.com</p>
                <p><strong>Telefone:</strong> (11) 99999-9999</p>
                <p><strong>Data de Nascimento:</strong> 15/08/1990</p>
                <p><strong>Gênero:</strong> Masculino</p>
                <p><strong>CPF:</strong> 888.888.888-08</p>
                <button onclick="openModal('addressModal')">Ver Endereços</button>
                <button onclick="openModal('cardModal')">Ver Cartões</button>
            </div>

            <!-- Modal de Endereços -->
            <div id="addressModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal('addressModal')">&times;</span>
                    <h2>Endereços Cadastrados</h2>

                    <button class="accordion">Rua Exemplo, 123 - São Paulo, SP</button>
                    <div class="panel">
                        <p><strong>Tipo:</strong> Apartamento</p>
                        <p><strong>Logradouro:</strong> Rua Exemplo</p>
                        <p><strong>Número:</strong> 123</p>
                        <p><strong>Bairro:</strong> Centro</p>
                        <p><strong>CEP:</strong> 01000-000</p>
                        <p><strong>Cidade:</strong> São Paulo</p>
                        <p><strong>Estado:</strong> SP</p>
                        <p><strong>País:</strong> Brasil</p>
                        <p><strong>Observação:</strong> Próximo ao metrô</p>
                    </div>

                    <button class="accordion">Av. Teste, 456 - Rio de Janeiro, RJ</button>
                    <div class="panel">
                        <p><strong>Tipo:</strong> Casa</p>
                        <p><strong>Logradouro:</strong> Av. Teste</p>
                        <p><strong>Número:</strong> 456</p>
                        <p><strong>Bairro:</strong> Copacabana</p>
                        <p><strong>CEP:</strong> 22000-000</p>
                        <p><strong>Cidade:</strong> Rio de Janeiro</p>
                        <p><strong>Estado:</strong> RJ</p>
                        <p><strong>País:</strong> Brasil</p>
                        <p><strong>Observação:</strong> Casa de Praia</p>
                    </div>
                </div>
            </div>
            <!-- Modal de Cartões -->
            <div id="cardModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal('cardModal')">&times;</span>
                    <h2>Cartões Cadastrados</h2>

                    <button class="accordion">**** **** **** 1234</button>
                    <div class="panel">
                        <p><strong>Nome Impresso:</strong> João Silva</p>
                        <p><strong>Bandeira:</strong> Visa</p>
                        <p><strong>Código de Segurança:</strong> 123</p>
                        <p><strong>Preferencial:</strong> Sim</p>
                    </div>

                    <button class="accordion">**** **** **** 5678</button>
                    <div class="panel">
                        <p><strong>Nome Impresso:</strong> João Silva</p>
                        <p><strong>Bandeira:</strong> MasterCard</p>
                        <p><strong>Código de Segurança:</strong> 456</p>
                        <p><strong>Preferencial:</strong> Não</p>
                    </div>
                </div>
            </div>
    `;

    document.getElementById("contaInfo").innerHTML = contaInfo;
});
