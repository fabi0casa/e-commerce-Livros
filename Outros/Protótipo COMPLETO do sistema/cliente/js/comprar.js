let totalCompra = 61.70; // Valor total original
let totalDesconto = 0; // Total de cupons usados

function openModal(id) {
    document.getElementById(id).style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

function aplicarCupom() {
    let cupomValor = parseFloat(document.getElementById("cupomSelect").value);

    if ((totalDesconto + cupomValor) >= totalCompra) {
        alert("O valor dos cupons não pode ultrapassar o total da compra.");
        return;
    }

    totalDesconto += cupomValor;
    let novoTotal = (totalCompra - totalDesconto).toFixed(2);

    document.getElementById("total-compra").innerText = `R$ ${novoTotal}`;
    
    closeModal("couponModal");
}
