-- Garante que o banco seja recriado a cada reinicialização, ótimo para desenvolvimento
DROP TABLE IF EXISTS produto, categoria, fornecedor, usuario_papel, usuario, papel;

CREATE TABLE categoria (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE fornecedor (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            nome VARCHAR(100) NOT NULL,
                            cnpj VARCHAR(18) NOT NULL UNIQUE
);

CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         login VARCHAR(100) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE papel (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE usuario_papel (
                               usuario_id BIGINT NOT NULL,
                               papel_id BIGINT NOT NULL,
                               PRIMARY KEY (usuario_id, papel_id),
                               FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                               FOREIGN KEY (papel_id) REFERENCES papel(id)
);

CREATE TABLE produto (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         quantidade INT NOT NULL,
                         preco DECIMAL(10, 2) NOT NULL,
                         categoria_id INT,
                         fornecedor_id INT,
                         FOREIGN KEY (categoria_id) REFERENCES categoria(id),
                         FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);
