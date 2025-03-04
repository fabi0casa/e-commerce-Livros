document.addEventListener("DOMContentLoaded", function() {
    const header = `
        <header>
            <a href="home.html" class="logo"></a>
            <input type="text" class="search-bar" placeholder="Buscar livros...">
            <div class="header-buttons">
                <button class="btn">
                    <img src="css/notification.png" alt="Notificações">
                    <span>Notificações</span>
                </button>
                <button class="btn" onclick="window.location.href='carrinho.html'">
                    <img src="css/cart.png" alt="Carrinho">
                    <span>Carrinho</span>
                </button>
                <button class="btn" onclick="window.location.href='conta.html'">
                    <img src="css/account.png" alt="Conta">
                    <span>Conta</span>
                </button>
            </div>            
        </header>
    `;
    
    document.body.insertAdjacentHTML("afterbegin", header);
});
