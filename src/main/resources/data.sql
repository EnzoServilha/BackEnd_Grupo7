INSERT INTO permissao (nome) VALUES ('ROLE_ADMIN');
INSERT INTO permissao (nome) VALUES ('ROLE_USER');

INSERT INTO usuario (nome, email, senha, data_cadastro, permissao_id)
VALUES ('Admin', 'admin@teste.com', '$2a$10$slYQmyNdgTY18LjhChABnuQnJZB1zr7rZoBkZWQB3A7t5HpP4EOUK', NOW(), 1);