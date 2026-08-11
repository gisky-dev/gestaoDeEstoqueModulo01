const appContent = document.getElementById('app-content');

// Templates HTML para cada "página"
const renderProdutos = async () => {
    try {
        const produtos = await api.getProdutos();
        const tableRows = produtos.map(p => `
            <tr>
                <td>${p.id}</td>
                <td>${p.nome}</td>
                <td>${p.quantidade}</td>
                <td>R$ ${p.preco.toFixed(2)}</td>
                <td><span class="badge badge-primary rounded-pill d-inline">${p.nomeCategoria}</span></td>
                <td>
                    <button class="btn btn-sm btn-danger btn-floating" data-id="${p.id}" data-action="delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');

        return `
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h2">Produtos em Estoque</h1>
                <a href="#/produtos/novo" class="btn btn-primary btn-rounded"><i class="fas fa-plus me-2"></i>Adicionar Produto</a>
            </div>
            <div class="card"><div class="card-body">
                <table class="table align-middle mb-0 bg-white table-hover">
                    <thead class="bg-light">
                        <tr><th>ID</th><th>Nome</th><th>Qtd.</th><th>Preço</th><th>Categoria</th><th>Ações</th></tr>
                    </thead>
                    <tbody>${tableRows}</tbody>
                </table>
            </div></div>
        `;
    } catch (error) {
        return `<div class="alert alert-danger">Erro ao carregar produtos.</div>`;
    }
};

const renderCategorias = async () => {
    // Lógica similar para buscar e renderizar categorias
    return '<h1>Página de Categorias (a implementar)</h1>';
};

// Mapa de rotas
const routes = {
    '/produtos': renderProdutos,
    '/categorias': renderCategorias,
    // Adicione outras rotas aqui
};

// Função principal do roteador
const router = async () => {
    // Proteção de rota: se não estiver logado, manda para o login.
    if (!auth.isLoggedIn()) {
        window.location.pathname = '/login.html';
        return;
    }

    // Pega o caminho do hash ou vai para a página padrão
    const path = window.location.hash.substring(1) || '/produtos';
    const renderFunction = routes[path] || (() => '<h2>Página não encontrada</h2>');

    appContent.innerHTML = await renderFunction();
};

// O roteador é acionado quando a página carrega ou o hash da URL muda
window.addEventListener('load', router);
window.addEventListener('hashchange', router);

// Delegação de eventos para botões de ação (ex: deletar)
appContent.addEventListener('click', async (event) => {
    const target = event.target.closest('[data-action="delete"]');
    if (target) {
        const id = target.dataset.id;
        if (confirm(`Tem certeza que deseja deletar o produto ID ${id}?`)) {
            try {
                await api.deleteProduto(id);
                alert('Produto deletado com sucesso!');
                router(); // Recarrega a view atual
            } catch (error) {
                alert('Erro ao deletar produto. Você tem permissão de ADMIN?');
            }
        }
    }
});
