document.addEventListener("DOMContentLoaded", function() {
    const contaAlterar = `
            <div id="alterar" class="secao">
                <h2>Alterar Dados</h2>
                <form class="formulario">
                    <label>Nome Completo:</label>
                    <input type="text" required>

                    <label>Data de Nascimento:</label>
                    <input type="date" required>

                    <label>CPF:</label>
                    <input type="text" required placeholder="000.000.000-00">

                    <label>Gênero:</label>
                    <select required>
                        <option value="">Selecione</option>
                        <option value="masculino">Masculino</option>
                        <option value="feminino">Feminino</option>
                        <option value="outro">Outro</option>
                        <option value="nao_informar">Prefiro não informar</option>
                    </select>

                    <h3>Contato</h3>
                    <label>Tipo de Telefone:</label>
                    <select required>
                        <option value="">Selecione</option>
                        <option value="celular">Celular</option>
                        <option value="residencial">Residencial</option>
                        <option value="comercial">Comercial</option>
                    </select>

                    <label>DDD:</label>
                    <input type="text" required placeholder="Ex: 11">

                    <label>Número:</label>
                    <input type="text" required placeholder="Ex: 99999-9999">

                    <label>Email:</label>
                    <input type="email" required>
                    <button>Salvar Alterações</button>
                </form>
            </div>
    `;

    document.getElementById("contaAlterar").innerHTML = contaAlterar;

    // Efeito de accordion
    let accordions = document.querySelectorAll(".accordion");
    accordions.forEach(button => {
        button.addEventListener("click", function () {
            let panel = this.nextElementSibling;
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });
    });
});
