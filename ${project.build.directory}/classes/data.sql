INSERT INTO permissao (nome) VALUES ('ROLE_ADMIN');
INSERT INTO permissao (nome) VALUES ('ROLE_USER');

INSERT INTO usuario (nome, email, senha, data_cadastro, permissao_id)
VALUES ('Admin', 'admin@teste.com', '$2a$12$ugv97T5SrNAZuvPU/siZ9.H2.GrvmEvnRKGwYAhNKgDBxNoC2Gie6', NOW(), 1);

-- 1. TIPOS, STATUS E CATEGORIAS (Básico)
INSERT INTO tipo (id, nome) VALUES (1, 'ENTRADA'), (2, 'SAIDA'), (3, 'AJUSTE');
INSERT INTO status (id, nome) VALUES (1, 'PENDENTE'), (2, 'CONCLUIDO'), (3, 'CANCELADO');
INSERT INTO categoria (nome) VALUES ('Peças Automotivas'), ('Ferramentas'), ('Eletrônicos');

-- 2. PERIODOS (Movi para cá para garantir que o ID 1 exista antes das movimentações)
INSERT INTO periodo (data_criacao, anotacao, qtd_pecas)
VALUES (NOW(), 'Fechamento do primeiro trimestre de estoque', 150); -- Este será o ID 1

INSERT INTO periodo (data_criacao, anotacao, qtd_pecas)
VALUES (NOW(), 'Inventário mensal - Setor de Amortecedores', 42);

INSERT INTO periodo (data_criacao, anotacao, qtd_pecas)
VALUES (NOW(), 'Reposição de estoque emergencial', 0);

-- 3. ENDEREÇOS
INSERT INTO endereco (cep, logradouro, numero, bairro, cidade, uf)
VALUES ('01001-000', 'Praça da Sé', '100', 'Centro', 'São Paulo', 'SP'),
       ('80010-000', 'Rua XV de Novembro', '500', 'Centro', 'Curitiba', 'PR');

-- 4. CLIENTES E FORNECEDORES
INSERT INTO cliente (nome_empresa, nome_contato, cpf_cnpj, telefone, email, data_cadastro, endereco_id)
VALUES ('Oficina do Jhow', 'Jhow Silva', '12.345.678/0001-99', '(11) 9999-8888', 'contato@jhow.com', NOW(), 1);

INSERT INTO fornecedor (razao_social, cnpj, nome_contato, nome_empresa, telefone, email, observacoes, data_cadastro, endereco_id)
VALUES ('Distribuidora de Pecas Brasil LTDA', '12.345.678/0001-90', 'Marcos Oliveira', 'Pecas & Cia', '(11) 4002-8922', 'contato@pecascia.com', 'Fornecedor principal de amortecedores', NOW(), 1);

INSERT INTO fornecedor (razao_social, cnpj, nome_contato, nome_empresa, telefone, email, observacoes, data_cadastro, endereco_id)
VALUES ('Importadora Global S.A.', '98.765.432/0001-11', 'Ana Costa', 'Global Imports', '(21) 3344-5566', 'vendas@global.com', 'Importado de pecas asiaticas', NOW(), 2);


-- 5. ITENS
INSERT INTO item (codigo_interno, marca, ano, descricao, localizacao)
VALUES ('AMOR-001', 'Cofap', 2022, 'Amortecedor Dianteiro Direito', 'Prateleira A1'),
       ('AMOR-002', 'Monroe', 2022, 'Amortecedor Dianteiro Esquerdo', 'Prateleira A1'),
       ('FILT-099', 'Fram', 2023, 'Filtro de Óleo - Modelo X', 'Corredor B');

INSERT INTO item_similar (fk_item, fk_item_similar) VALUES (1, 2), (2, 1);

-- 6. MOVIMENTAÇÕES (Agora o periodo_id 1 existe!)
INSERT INTO movimentacao_estoque (fk_usuario, tipo_id, status_id, periodo_id, numero_nota_fiscal)
VALUES
(1, 1, 2, 1, 'NF-2026-P1'),
(1, 1, 2, 2, 'NF-2026-P2'),
(1, 1, 2, 3, 'NF-2026-P3');

INSERT INTO itens_na_movimentacao (movimentacao_estoque_id, item_id, qtd, preco_unitario)
VALUES
(1, 1, 150, 150.00),
(2, 1, 42, 150.00),
(3, 1, 10, 150.00);

-- 7. FECHAMENTO
INSERT INTO fechamento_mes (mes, ano, qtd, fk_item) VALUES (1, 2026, 50, 1);
