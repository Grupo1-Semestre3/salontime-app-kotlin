use salon_time;

INSERT INTO status_agendamento (status) VALUES
('AGENDADO'),
('CANCELADO'),
('AUSENTE'),
('PAGAMENTO_PENDENTE'),
('CONCLUIDO');

INSERT INTO tipo_usuario (descricao) VALUES
('ADMINISTRADOR'),
('CLIENTE'),
('FUNCIONARIO');

-- ADMIN
INSERT INTO usuario (tipo_usuario_id, nome, telefone, CPF, email, senha, login, foto, data_nascimento)
VALUES
(1, 'Admin Master', '11999999999', '00000000000', 'admin@salontime.com', 'admin123', 1, NULL, '1980-01-01');

-- FUNCIONÁRIOS
INSERT INTO usuario (tipo_usuario_id, nome, telefone, CPF, email, senha, login, foto, data_nascimento)
VALUES
(3, 'Joana Souza', '11988887777', '12345678900', 'joana@salontime.com', 'joana123', 0, NULL, '1990-05-15');

INSERT INTO usuario (tipo_usuario_id, nome, telefone, CPF, email, senha, login, foto, data_nascimento)
VALUES
(3, 'Marina', '11988887777', '12345678900', 'joana@salontime.com', 'joana123', 0, NULL, '1990-05-15');

-- CLIENTES
INSERT INTO usuario (tipo_usuario_id, nome, telefone, CPF, email, senha, login, foto, data_nascimento)
VALUES
(2, 'Maria Clara', '11966665555', '34567890123', 'maria@cliente.com', 'maria123', 0, NULL, '1995-12-20'),
(2, 'Lucas Lima', '11955554444', '45678901234', 'lucas@cliente.com', 'lucas123', 0, NULL, '2000-03-10');

INSERT INTO funcionamento (dia_semana, inicio, fim, aberto, capacidade, funcionario_id) VALUES
('TUESDAY', '10:00:00', '19:00:00', 1, 2, 1),
('WEDNESDAY', '10:00:00', '19:00:00', 1, 2, 1),
('THURSDAY', '10:00:00', '19:00:00', 1, 2, 1),
('FRIDAY', '10:00:00', '19:00:00', 1, 2, 1),
('SATURDAY', '10:00:00', '19:00:00', 1, 2, 1),
('SUNDAY', NULL, NULL, 0, NULL, 1),
('MONDAY', NULL, NULL, 0, NULL, 1);

INSERT INTO funcionamento (dia_semana, inicio, fim, aberto, capacidade, funcionario_id) VALUES
('TUESDAY', '10:00:00', '19:00:00', 1, 2, 2),
('WEDNESDAY', '10:00:00', '19:00:00', 1, 2, 2),
('THURSDAY', '10:00:00', '19:00:00', 1, 2, 2),
('FRIDAY', '10:00:00', '19:00:00', 1, 2, 2),
('SATURDAY', '10:00:00', '19:00:00', 1, 2, 2),
('SUNDAY', NULL, NULL, 0, NULL, 2),
('MONDAY', NULL, NULL, 0, NULL, 2);

INSERT INTO funcionamento (dia_semana, inicio, fim, aberto, capacidade, funcionario_id) VALUES
('TUESDAY', '10:00:00', '19:00:00', 1, 1, 3),
('WEDNESDAY', '10:00:00', '19:00:00', 1, 1, 3),
('THURSDAY', '10:00:00', '19:00:00', 1, 1, 3),
('FRIDAY', '10:00:00', '19:00:00', 1, 1, 3),
('SATURDAY', '10:00:00', '19:00:00', 1, 1, 3),
('SUNDAY', NULL, NULL, 0, NULL, 3),
('MONDAY', NULL, NULL, 0, NULL, 3);


INSERT INTO servico (nome, preco, tempo, status, simultaneo, descricao, foto)
VALUES
('Corte Feminino', 70.00, '00:45:00', 'ATIVO', 1, 'Corte feminino completo', NULL),
('Corte Masculino', 50.00, '00:30:00', 'ATIVO', 1, 'Corte masculino tradicional', NULL),
('Manicure', 40.00, '00:40:00', 'ATIVO', 0, 'Serviço de manicure', NULL),
('Luzes top', 150.00, '02:00:00', 'ATIVO', 0, 'Luzes especiais', NULL),
('Dia da Noiva', 200.00, '03:00:00', 'ATIVO', 1, 'Pacote especial para noivas', NULL);

INSERT INTO pagamento (forma, taxa) VALUES
('Dinheiro', 0.00),
('Cartão de Crédito', 3.50),
('Pix', 0.00);


INSERT INTO funcionario_competencia (funcionario_id, servico_id)
VALUES
(1, 1), -- Corte Feminino
(1, 2), -- Corte Masculino
(1, 3), -- Manicure
(1, 4), -- Manicure
(1, 5); -- Manicure

INSERT INTO funcionario_competencia (funcionario_id, servico_id)
VALUES
(2, 1), -- Corte Feminino
(2, 2), -- Corte Masculino
(2, 3), -- Manicure
(2, 4), -- Manicure
(2, 5); -- Manicure

INSERT INTO funcionario_competencia (funcionario_id, servico_id)
VALUES
(3, 1), -- Corte Feminino
(3, 2), -- Corte Masculino
(3, 3), -- Manicure
(3, 4), -- Manicure
(3, 5); -- Manicure


INSERT INTO agendamento (funcionario_id, servico_id, usuario_id, status_agendamento_id, pagamento_id, data, inicio, fim, preco)
VALUES
(3, 2, 4, 1, 2, '2025-05-27', '10:00:00', '10:30:00', 50.00),
(3, 2, 4, 1, 2, '2025-05-27', '15:00:00', '15:30:00', 50.00);

INSERT INTO agendamento (funcionario_id, servico_id, usuario_id, status_agendamento_id, pagamento_id, data, inicio, fim, preco)
VALUES
(5, 1, 4, 1, 1, '2025-06-26', '21:00:00', '22:00:00', 40.00);

INSERT INTO avaliacao (agendamento_id, usuario_id, nota_servico, descricao_servico, data_horario)
VALUES
(1, 4, 5, 'Excelente atendimento e corte impecável!', NOW()),
(2, 4, 4, 'Bom serviço, mas poderia ser mais rápido.', NOW());


INSERT INTO horario_excecao (
    data_inicio,
    data_fim,
    inicio,
    fim,
    aberto,
    capacidade,
    funcionario_id
) VALUES (
    '2025-03-20',
    '2025-08-20',
    '08:00:00',
    '23:00:00',
    1,
    2,
    5
);


INSERT INTO info_salao (email, telefone, logradouro, numero, cidade, estado, complemento)
VALUES ('contato@salaoexemplo.com', '11987654321', 'Rua das Flores', '123', 'São Paulo', 'SP', 'Próximo ao metrô');

INSERT INTO cupom (nome, descricao, codigo, ativo, inicio, fim, tipo_destinatario)
VALUES
('Desconto Black Friday', 'Desconto de 30% na Black Friday', 'BLACK30', 1, '2025-11-25', '2025-11-30', 'clientes');

INSERT INTO cupom (nome, descricao, codigo, ativo, inicio, fim, tipo_destinatario)
VALUES
('Frete Grátis', 'Frete grátis para compras acima de R$100', 'FRETEGRATIS', 1, '2025-05-01', '2025-06-01', 'todos');

INSERT INTO cupom (nome, descricao, codigo, ativo, inicio, fim, tipo_destinatario)
VALUES
('Desconto Aniversário', '20% para aniversariantes do mês', 'ANIV20', 1, '2025-01-01', '2025-12-31', 'clientes');




