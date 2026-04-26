INSERT INTO permissao (nome) VALUES ('ROLE_ADMIN');
INSERT INTO permissao (nome) VALUES ('ROLE_USER');

INSERT INTO usuario (nome, email, senha, data_cadastro, permissao_id)
VALUES ('Admin', 'admin@teste.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC', NOW(), 1);

