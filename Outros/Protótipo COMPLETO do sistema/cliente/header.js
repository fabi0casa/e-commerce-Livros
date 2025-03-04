document.addEventListener("DOMContentLoaded", function() {
    const header = `
        <header>
            <a href="home.html" class="logo"></a>
            <input type="text" class="search-bar" placeholder="Buscar livros..." id="searchInput">
            <div class="header-buttons">
                <button class="btn" id="notificationBtn">
                    <img src="css/notification.png" alt="Notificações">
                    <span>Notificações</span>
                    <span class="notification-badge" id="notificationBadge">3</span>
                </button>
                <button class="btn" onclick="window.location.href='carrinho.html'">
                    <img src="css/cart.png" alt="Carrinho">
                    <span>Carrinho</span>
                    <span class="cart-badge">2</span>
                </button>
                <button class="btn" onclick="window.location.href='conta.html'">
                    <img src="css/account.png" alt="Conta">
                    <span>Conta</span>
                </button>
            </div>
            <div class="notification-popup" id="notificationPopup">
                <ul id="notificationList">
                    <li>
                        <div class="notification-title">⚠️ Carrinho</div>
                        <span>Seus itens no carrinho foram removidos por ultrapassarem o limite de tempo</span>
                        <button class="ok-btn">OK</button>
                    </li>
                    <li>
                        <div class="notification-title">✅ Troca</div>
                        <span>A troca do seu pedido #1850 foi autorizada!</span>
                        <button class="ok-btn">OK</button>
                    </li>
                    <li>
                        <div class="notification-title">⚠️ Carrinho</div>
                        <span>Um item foi removido do seu carrinho devido à falta no estoque.</span>
                        <button class="ok-btn">OK</button>
                    </li>
                </ul>
            </div>
        </header>
    `;
    
    document.body.insertAdjacentHTML("afterbegin", header);

    // Redirecionamento ao pressionar Enter na barra de busca
    document.getElementById("searchInput").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            let query = encodeURIComponent(this.value);
            window.location.href = "buscar.html?q=" + query;
        }
    });

    const notificationBtn = document.getElementById("notificationBtn");
    const notificationPopup = document.getElementById("notificationPopup");
    const notificationBadge = document.getElementById("notificationBadge");
    const notificationList = document.getElementById("notificationList");

    function updateNotificationCount() {
        let count = notificationList.children.length;
        if (count > 0) {
            notificationBadge.textContent = count;
            notificationBadge.style.display = "block";
        } else {
            notificationBadge.style.display = "none";
        }
    }

    notificationBtn.addEventListener("click", function(event) {
        event.stopPropagation();
        notificationPopup.classList.toggle("active");
    });

    document.addEventListener("click", function(event) {
        if (!notificationBtn.contains(event.target) && !notificationPopup.contains(event.target)) {
            notificationPopup.classList.remove("active");
        }
    });

    document.querySelectorAll(".ok-btn").forEach(button => {
        button.addEventListener("click", function() {
            this.parentElement.remove();
            updateNotificationCount();
        });
    });

    updateNotificationCount();
});
