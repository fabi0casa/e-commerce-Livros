document.addEventListener("DOMContentLoaded", function() {
    const contaCartao = `
             <div id="cartao" class="secao">
                <h2>Cadastrar Novo Cartão</h2>
                <form class="formulario">
                    <label>Número do Cartão:</label>
                    <input type="text" required>
        
                    <label>Nome Impresso:</label>
                    <input type="text" required>
        
                    <label>Bandeira:</label>
                    <select required>
                        <option value="">Selecione</option>
                        <option value="Visa">Visa</option>
                        <option value="Elo">Elo</option>
                        <option value="MasterCard">Mastercard</option>
                    </select>
        
                    <label>Código de segurança:</label>
                    <input type="text" required>
        
                    <label>Tornar esse cartão preferencial?</label>
                    <select required>
                        <option value="">Selecione</option>
                        <option value="sim">Sim</option>
                        <option value="nao">Não</option>
                    </select>
                    <button>Salvar Cartão</button>
                </form>
            </div>
    `;

    document.getElementById("contaCartao").innerHTML = contaCartao;
});
