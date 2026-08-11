// Módulo para centralizar todas as chamadas à API
const api = {
    // Função auxiliar genérica para requisições
    fetch: async (endpoint, method = 'GET', body = null) => {
        const token = auth.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const config = {
            method: method,
            headers: headers
        };

        if (body) {
            config.body = JSON.stringify(body);
        }

        const response = await fetch(`/api${endpoint}`, config);

        // Se o token for inválido/expirado, o servidor retornará 403.
        // Devemos deslogar o usuário.
        if (response.status === 403 || response.status === 401) {
            auth.handleLogout();
            return;
        }

        if (!response.ok) {
            throw new Error(`Erro na API: ${response.statusText}`);
        }

        // Retorna um JSON vazio para respostas 204 No Content
        if (response.status === 204) {
            return {};
        }

        return response.json();
    },

    // Funções específicas para cada recurso
    getProdutos: () => api.fetch('/produtos'),
    deleteProduto: (id) => api.fetch(`/produtos/${id}`, 'DELETE'),

    getCategorias: () => api.fetch('/categorias'),
    // ... adicione outras funções conforme necessário (getFornecedores, createProduto, etc.)
};
