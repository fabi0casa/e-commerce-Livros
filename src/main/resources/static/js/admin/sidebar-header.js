export function loadSidebar() {
  renderHeader();
  renderSidebar();
  attachSidebarToggle();
  attachOverlayHandler();
}

function renderHeader() {
  const headerHTML = `
    <header class="admin-header">
      <a href="/dashboard"><img src="/img/home.png" alt="home" class="header-logo"></a>
      <span>Página do Administrador</span>
    </header>
  `;
  document.body.insertAdjacentHTML('afterbegin', headerHTML);
}

function renderSidebar() {
  const sidebarHTML = `
    <div id="sidebar-overlay"></div>
    <div id="custom-sidebar" class="sidebar">
      <div class="sidebar-header">
        <img src="/img/logo.png" alt="Logo" class="logo-icon mb-2">
        <h4 class="admin-title">Admin</h4>
      </div>
      <nav class="sidebar-nav">
        <a href="/dashboard">Dashboard</a>
        <a href="/gerenciar-clientes">Gerenciar Clientes</a>
        <a href="#">Gerenciar Livros</a>
        <a href="/gerenciar-pedidos">Vendas Eletrônicas</a>
        <a href="/estoque">Controle de Estoque</a>
        <a href="/analise">Análise de Categorias</a>
        <a href="/home">Acessar Livraria</a>
        <a href="/cadastrar-admin">Cadastrar Admin</a>
      </nav>
    </div>
  `;
  document.body.insertAdjacentHTML('beforeend', sidebarHTML);
}

function attachSidebarToggle() {
  const homeButton = document.querySelector('.header-logo');
  const sidebar = document.getElementById('custom-sidebar');
  const overlay = document.getElementById('sidebar-overlay');

  if (homeButton && sidebar && overlay) {
    homeButton.addEventListener('click', (e) => {
      e.preventDefault();
      const isVisible = sidebar.classList.toggle('visible');
      overlay.classList.toggle('visible', isVisible);
    });
  }
}

function attachOverlayHandler() {
  const overlay = document.getElementById('sidebar-overlay');
  const sidebar = document.getElementById('custom-sidebar');

  if (overlay && sidebar) {
    overlay.addEventListener('click', () => {
      sidebar.classList.remove('visible');
      overlay.classList.remove('visible');
    });
  }
}
