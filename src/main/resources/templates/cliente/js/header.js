document.addEventListener("DOMContentLoaded", function() {
    const header = `
        <header>
			<a href="/cliente/home" class="logo"></a>
            <input type="text" class="search-bar" placeholder="Buscar livros..." id="searchInput">
            <div class="header-buttons">
                <button class="btn" id="notificationBtn">
										<img src="/img//notification.png" alt="Notificações">
                    <span>Notificações</span>
                    <span class="notification-badge" id="notificationBadge">3</span>
                </button>
                <button class="btn" onclick="window.location.href='carrinho.html'">
										<img src="/img//cart.png" alt="Carrinho">
                    <span>Carrinho</span>
                    <span class="cart-badge">2</span>
                </button>
                <button class="btn" onclick="window.location.href='conta.html'">
										<img src="/img//account.png" alt="Conta">
                    <span id="accountText" >Conta</span>
                </button>
            </div>
            <div class="notification-popup" id="notificationPopup">
                <ul id="notificationList">
                    <li>
                        <div class="notification-title">⚠️ Carrinho</div>
                        <span>O produto 'Senhor dos Anéis' no seu carrinho foi removido por ultrapassar o limite de tempo de 5 dias</span>
                        <button class="ok-btn">OK</button>
                    </li>
                    <li>
                        <div class="notification-title">✅ Troca</div>
                        <span>A troca do seu pedido (1x Dom Quixote) foi autorizada!</span>
                        <button class="ok-btn">OK</button>
                    </li>
                    <li>
                        <div class="notification-title">⚠️ Carrinho</div>
                        <span>O Produto 'Diário de um Banana 7' foi removido do seu carrinho devido à falta no estoque.</span>
                        <button class="ok-btn">OK</button>
                    </li>
                </ul>
            </div>
        </header>
    `;

    document.body.insertAdjacentHTML("afterbegin", header);

      function atualizarBotaoConta(nomeCliente) {
        if (nomeCliente) {
            const primeiroNome = nomeCliente.split(" ")[0]; // Pega apenas o primeiro nome
            document.getElementById("accountText").textContent = primeiroNome;
        }
    }

    // Se o clienteId foi definido no HTML pelo Thymeleaf, já usamos ele para evitar a requisição
    if (typeof clienteId !== "undefined" && clienteId !== null) {
        fetch(`/clientes/${clienteId}`)  // Ajuste o endpoint conforme necessário
            .then(response => response.json())
            .then(data => {
                if (data && data.nome) {
                    atualizarBotaoConta(data.nome);
                }
            })
            .catch(error => console.log("Erro ao buscar cliente:", error));
    }

    // Redirecionamento ao pressionar Enter na barra de busca
    document.getElementById("searchInput").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            let query = encodeURIComponent(this.value);
            window.location.href = "/cliente/buscar?q=" + query;
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
