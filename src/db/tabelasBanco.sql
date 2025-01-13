USE desafioUOL;

CREATE TABLE estoque
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(50),
    valor DECIMAL(10, 2) NOT NULL,
    quantidade INT NOT NULL
);

CREATE TABLE carrinho
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    valor_total DECIMAL(10, 2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE produto_carrinho 
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_carrinho INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_carrinho) REFERENCES carrinho(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES estoque(id) ON DELETE CASCADE
);

INSERT INTO estoque (nome, descricao, categoria, valor, quantidade)
VALUES 
    ('Notebook', 'Notebook Dell Inspiron', 'Eletrônicos', 3500.00, 10),
    ('Smartphone', 'iPhone 13', 'Eletrônicos', 6999.99, 5),
    ('Livro', 'Clean Code', 'Livros', 89.90, 50),
    ('Mouse', 'Mouse Logitech', 'Periféricos', 120.00, 30);