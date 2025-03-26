let totalCompra = 61.70; // Valor total original
let totalDesconto = 0; // Total de cupons usados
let cuponsAplicados = []; // Lista de cupons usados

function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

function aplicarCupom() {
    let selectElement = document.getElementById("cupomSelect");
    let cupomValor = parseFloat(selectElement.value);
    let cupomTexto = selectElement.options[selectElement.selectedIndex].text;

    if (totalCompra - totalDesconto <= 0) {
        alert("Não é possível aplicar mais cupons. O valor da compra já está zerado.");
        return;
    }

    totalDesconto += cupomValor;
    let novoTotal = (totalCompra - totalDesconto).toFixed(2);
    // Se o total for menor que 0, zera ele
    if (novoTotal < 0) {
        novoTotal = 0;
    }

    // Adicionar visualmente o desconto ao extrato
    let extratoDiv = document.querySelector(".cupom");
    let descontoItem = document.createElement("div");
    descontoItem.innerHTML = `<h4>- R$ ${cupomValor.toFixed(2)} - Cupom</h4>`;
    extratoDiv.insertBefore(descontoItem, extratoDiv.firstChild);

    document.getElementById("total-compra").innerText = `R$ ${novoTotal}`;

    // Remover o cupom já utilizado do select
    cuponsAplicados.push(cupomValor);
    selectElement.remove(selectElement.selectedIndex);

    // Fechar modal
    closeModal("couponModal");
}

function toggleInput(id) {
    let input = document.getElementById(id);
    input.style.display = input.style.display === 'none' ? 'inline-block' : 'none';
}
