document.addEventListener("DOMContentLoaded", function() {
    const contaEndereco = `
             <div id="endereco" class="secao">
                <h2>Cadastrar Novo Endereço</h2>
                <form class="formulario">
                    <label>Tipo de Residência:</label>
                    <select required>
                        <option value="">Selecione</option>
                        <option value="casa">Casa</option>
                        <option value="apartamento">Apartamento</option>
                        <option value="outro">Outro</option>
                    </select>
        
                    <label>Tipo de Logradouro:</label>
                    <input type="text" required placeholder="Ex: Rua, Avenida...">
        
                    <label>Logradouro:</label>
                    <input type="text" required>
        
                    <label>Número:</label>
                    <input type="text" required>
        
                    <label>Bairro:</label>
                    <input type="text" required>
        
                    <label>CEP:</label>
                    <input type="text" required placeholder="00000-000">
        
                    <label>Cidade:</label>
                    <input type="text" required>
        
                    <label>Estado:</label>
                    <input type="text" required>
        
                    <label>País:</label>
                    <input type="text" required value="Brasil">
                    
                    <label>Observação (Opcional):</label>
                    <textarea></textarea>
        
                    <h3>Tipos de Endereço</h3>
                    <label>Usar como Endereço de Residência?</label>
                    <select id="residencia" required>
                        <option value="">Selecione</option>
                        <option value="sim">Sim</option>
                        <option value="nao">Não</option>
                    </select>
        
                    <label>Usar como Endereço de Entrega?</label>
                    <select id="entrega" required>
                        <option value="">Selecione</option>
                        <option value="sim">Sim</option>
                        <option value="nao">Não</option>
                    </select>
        
                    <label>Usar como Endereço de Cobrança?</label>
                    <select id="cobranca" required>
                        <option value="">Selecione</option>
                        <option value="sim">Sim</option>
                        <option value="nao">Não</option>
                    </select>
        
                    <h6>*O endereço precisa ser pelo menos uma dessas opções acima</h6>
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
});
