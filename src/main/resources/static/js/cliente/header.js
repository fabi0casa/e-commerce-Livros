document.addEventListener("DOMContentLoaded", function() {
    const header = `
        <header>
			<a href="/home" class="logo"></a>
            <input type="text" class="search-bar" placeholder="Buscar livros..." id="searchInput">
            <div class="header-buttons">
                <button class="btn" id="notificationBtn">
										<img src="/img//notification.png" alt="Notificações">
                    <span>Notificações</span>
                    <span class="notification-badge" id="notificationBadge">3</span>
                </button>
                <button class="btn" onclick="window.location.href='/carrinho'">
										<img src="/img//cart.png" alt="Carrinho">
                    <span>Carrinho</span>
                    <span class="cart-badge" id="cartBadge">0</span> <!-- Começa como 0 -->
                </button>
                <button class="btn" onclick="window.location.href='/conta'">
                    <span class="account-avatar" id="accountAvatar">
                        <img src="/img/account.png/" alt="Conta">
                    </span>
                    <span id="accountText">Conta</span>
                </button>
            </div>
            <div class="notification-popup" id="notificationPopup">
                <ul id="notificationList">
                </ul>
            </div>
        </header>
    `;

    document.body.insertAdjacentHTML("afterbegin", header);

    function atualizarBotaoConta(nomeCliente) {
        if (nomeCliente) {
            const primeiroNome = nomeCliente.split(" ")[0];
            const inicial = primeiroNome.charAt(0);
    
            document.getElementById("accountText").textContent = primeiroNome;
    
            const avatar = document.getElementById("accountAvatar");
    
            // Gera uma cor determinística baseada na inicial
            const charCode = inicial.charCodeAt(0);
            const hue = (charCode * 15) % 360;  // Multiplica pra espaçar bem, e pega o resto de 360
            const corVibrante = `hsl(${hue}, 100%, 45%)`;
    
            // Cria o círculo com a inicial
            avatar.innerHTML = `<div class="account-circle" style="background-color: ${corVibrante};">${inicial}</div>`;
        }
    }

    function atualizarCarrinho() {
        const cartBadge = document.getElementById("cartBadge");
        if (!cartBadge) return;
    
        fetch(`/carrinho/quantidade`)
            .then(response => {
                if (response.status === 401) {
                    cartBadge.textContent = "0";
                    return;
                }
                if (!response.ok) throw new Error("Erro ao buscar quantidade do carrinho");
                return response.text(); // Aqui recebemos apenas um número, não JSON
            })
            .then(text => {
                cartBadge.textContent = text || "0";
            })
            .catch(error => {
                console.error("Erro ao buscar quantidade do carrinho:", error);
                cartBadge.textContent = "0";
            });
    }
    
    fetch("/clientes/me")
    .then(response => {
        if (response.status === 401) {
            // Não está logado, não tenta atualizar nada
            return null;
        }
        return response.json();
    })
    .then(data => {
        if (data && data.nome) {
            atualizarBotaoConta(data.nome);
            atualizarCarrinho(); // Só atualiza se estiver logado
        }
    })
    .catch(error => console.log("Erro ao buscar cliente logado:", error));


    // Redirecionamento ao pressionar Enter na barra de busca
    document.getElementById("searchInput").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            let query = encodeURIComponent(this.value);
            window.location.href = "/buscar?q=" + query;
        }
    });

    // Buscar e exibir notificações não vistas
    fetch('/notificacoes/nao-vistas')
        .then(response => response.json())
        .then(notificacoes => {
            notificationList.innerHTML = ""; // Limpa notificações fixas
            notificacoes.forEach(n => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <div class="notification-title">${n.titulo}</div>
                    <span>${n.descricao}</span>
                    <button class="ok-btn" data-id="${n.id}">OK</button>
                `;
                notificationList.appendChild(li);
            });

            updateNotificationCount();

            // Adiciona listener a cada botão OK
            document.querySelectorAll(".ok-btn").forEach(button => {
                button.addEventListener("click", function() {
                    const id = this.getAttribute("data-id");

                    fetch(`/notificacoes/${id}/marcar-visto`, {
                        method: "PUT"
                    }).then(() => {
                        this.parentElement.remove();
                        updateNotificationCount();
                    }).catch(err => {
                        console.error("Erro ao marcar notificação como vista:", err);
                    });
                });
            });
        })
        .catch(error => {
            console.error("Erro ao buscar notificações:", error);
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
            notificationPopup.classList.remove("active");
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
