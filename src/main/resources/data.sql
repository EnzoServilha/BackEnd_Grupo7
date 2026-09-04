-- DADOS OBRIGATORIOS
INSERT INTO permissao (nome) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
INSERT INTO tipo (nome) VALUES ('ENTRADA'), ('SAIDA'), ('AJUSTE'), ('COTACAO');
INSERT INTO status (nome) VALUES ('PENDENTE'), ('CONCLUIDO'), ('CONCLUIDO PARCIAL'), ('CANCELADO');

-- DADOS DE TESTE / DEMONSTRACAO
INSERT INTO usuario (nome, email, senha, data_cadastro, permissao_id, ativo)
VALUES
    ('Admin', 'admin@teste.com', '$2a$12$ugv97T5SrNAZuvPU/siZ9.H2.GrvmEvnRKGwYAhNKgDBxNoC2Gie6', CURRENT_TIMESTAMP, 1, true),
    ('Enzo', 'enzo@teste.com', '$2a$12$nZ2S4UeZfdZkbWIRAJ5aFOBMtFBZp/dSnvJxftI6hrLx9aXO4Lcte', CURRENT_TIMESTAMP, 2, true);

-- PERIODOS (garantir IDs antes das movimentacoes)
INSERT INTO periodo (data_criacao, anotacao, qtd_pecas, fechado, data_fechamento)
VALUES (CURRENT_TIMESTAMP, 'Fechamento do primeiro trimestre de estoque', 150, TRUE, CURRENT_TIMESTAMP);

INSERT INTO periodo (data_criacao, anotacao, qtd_pecas, fechado, data_fechamento)
VALUES (CURRENT_TIMESTAMP, 'Inventário mensal - Setor de Amortecedores', 42, TRUE, CURRENT_TIMESTAMP);

INSERT INTO periodo (data_criacao, anotacao, qtd_pecas, fechado)
VALUES (CURRENT_TIMESTAMP, 'Reposição de estoque emergencial', 0, FALSE);

-- ENDEREÇOS
INSERT INTO endereco (cep, logradouro, numero, bairro, cidade, uf, ativo) VALUES
    ('01001-000', 'Praça da Sé', '100', 'Centro', 'São Paulo', 'SP', true),
    ('80010-000', 'Rua XV de Novembro', '500', 'Centro', 'Curitiba', 'PR', true);

-- CLIENTES E FORNECEDORES
INSERT INTO cliente (nome_empresa, nome_contato, cpf_cnpj, telefone, email, data_cadastro, endereco_id, ativo)
VALUES ('Oficina do Jhow', 'Jhow Silva', '12.345.678/0001-99', '(11) 9999-8888', 'contato@jhow.com', CURRENT_TIMESTAMP, 1, true);

INSERT INTO fornecedor (razao_social, cnpj, nome_contato, nome_empresa, telefone, email, observacoes, data_cadastro, endereco_id, ativo)
VALUES ('Distribuidora de Pecas Brasil LTDA', '12.345.678/0001-90', 'Marcos Oliveira', 'Pecas & Cia', '(11) 4002-8922', 'contato@pecascia.com', 'Fornecedor principal de amortecedores', CURRENT_TIMESTAMP, 1, true);

INSERT INTO fornecedor (razao_social, cnpj, nome_contato, nome_empresa, telefone, email, observacoes, data_cadastro, endereco_id, ativo)
VALUES ('Importadora Global S.A.', '98.765.432/0001-11', 'Ana Costa', 'Global Imports', '(21) 3344-5566', 'vendas@global.com', 'Importado de pecas asiaticas', CURRENT_TIMESTAMP, 2, true);

-- ITENS
INSERT INTO item (codigo_interno, marca, ano, descricao, localizacao, ativo)
VALUES ('AMOR-001', 'Cofap', 2022, 'Amortecedor Dianteiro Direito', 'Prateleira A1', true),
       ('AMOR-002', 'Monroe', 2022, 'Amortecedor Dianteiro Esquerdo', 'Prateleira A1', true),
       ('FILT-099', 'Fram', 2023, 'Filtro de Óleo - Modelo X', 'Corredor B', true);

INSERT INTO item_similar (fk_item, fk_item_similar) VALUES (1, 2), (2, 1);

-- MOVIMENTAÇÕES
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

-- CATEGORIAS
INSERT INTO categoria (nome, ativo) VALUES
    ('Peças Automotivas', true),
    ('Ferramentas', true),
    ('Eletrônicos', true);

-- MARCAS DE TESTE
INSERT INTO marca (nome_empresa, ativo) VALUES ('Cofap', true), ('Monroe', true);
