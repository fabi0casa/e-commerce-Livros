
function verificarPermissaoAdmin() {
    fetch('/clientes/me')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar dados do cliente');
            }
            return response.json();
        })
        .then(cliente => {
            if (cliente.admin) {
                document.getElementById('dashboardBtn').style.display = 'flex';
            } else {
                document.getElementById('dashboardBtn').style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            // Em caso de erro, podemos esconder o botão por segurança
            document.getElementById('dashboardBtn').style.display = 'none';
        });
}

// Chama a função quando a página carrega
document.addEventListener('DOMContentLoaded', verificarPermissaoAdmin);