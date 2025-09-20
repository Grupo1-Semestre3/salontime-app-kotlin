create database salon_time;

-- drop database salon_time;

use salon_time;


show tables;

-- Criação das tabelas
CREATE TABLE info_salao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255),
    telefone VARCHAR(11),
    logradouro VARCHAR(100),
    numero VARCHAR(10),
    cidade VARCHAR(45),
    estado VARCHAR(45),
    complemento VARCHAR(45)
);

CREATE TABLE cupom (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45),
    descricao VARCHAR(45),
    codigo VARCHAR(45),
    ativo TINYINT,
    inicio DATE,
    fim DATE,
    tipo_destinatario VARCHAR(45)
);

CREATE TABLE tipo_usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descricao varchar(45)
);

CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo_usuario_id INT,
    nome VARCHAR(50),
    telefone CHAR(11),
    CPF CHAR(14),
    email VARCHAR(255),
    senha VARCHAR(30),
    login tinyint,
    foto longblob,
    data_nascimento Date,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    ativo tinyint default true,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

CREATE TABLE servico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50),
    preco DECIMAL(10,2),
    tempo TIME,
    status enum("ATIVO", "INATIVO"),
    simultaneo TINYINT,
    descricao varchar(255),
    foto longblob
);

CREATE TABLE funcionario_competencia(
	id INT primary key auto_increment,
    funcionario_id INT,
    servico_id INT,
    FOREIGN KEY (servico_id) REFERENCES servico(id),
    FOREIGN KEY (funcionario_id) REFERENCES usuario(id)
);

CREATE TABLE status_agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status enum("AGENDADO","CANCELADO","AUSENTE","PAGAMENTO_PENDENTE","CONCLUIDO")
);

create table pagamento(
	id INT PRIMARY KEY AUTO_INCREMENT,
    forma varchar(50),
    taxa decimal(10,2)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    funcionario_id INT,
    servico_id INT,
    usuario_id INT,
    cupom_id INT,
    status_agendamento_id INT,
    pagamento_id INT,
    data DATE,
    inicio TIME,
    fim TIME,
    preco DECIMAL(10,2),
    FOREIGN KEY (funcionario_id) REFERENCES usuario(id),
    FOREIGN KEY (cupom_id) REFERENCES cupom(id),
    FOREIGN KEY (servico_id) REFERENCES servico(id),
    FOREIGN KEY (status_agendamento_id) REFERENCES status_agendamento(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (pagamento_id) REFERENCES pagamento(id)
);



CREATE TABLE historico_agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_horario DATETIME,
    agendamento_id INT,
    agendamento_funcionario_id INT,
    agendamento_servico_id INT,
    agendamento_usuario_id INT,
    agendamento_status_agendamento_id INT,
    agendamento_pagamento_id INT,
    taxa_utilizada DECIMAL(5,2),
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id),
    inicio time,
    fim time,
    preco decimal(10,2)
);

CREATE TABLE funcionamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dia_semana ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'),
    inicio TIME,
    fim TIME,
    aberto TINYINT,
    capacidade INT,
    funcionario_id INT,
    FOREIGN KEY (funcionario_id) REFERENCES usuario(id)
);


CREATE TABLE desc_cancelamento(
	id INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(200),
    agendamento_id INT,
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id)
);

CREATE TABLE horario_excecao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_inicio DATE,
    data_fim DATE,
    inicio TIME,
    fim TIME,
    aberto TINYINT,
    capacidade INT,
	funcionario_id INT,
    FOREIGN KEY (funcionario_id) REFERENCES usuario(id)
);


CREATE TABLE avaliacao (
	id INT PRIMARY KEY auto_increment,
    agendamento_id INT,
    usuario_id INT,
    nota_servico INT,
    descricao_servico VARCHAR(255),
    data_horario DATETIME,
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);


CREATE TABLE cupom_destinado (
    id INT PRIMARY KEY auto_increment,
    cupom_id INT,
    usuario_id INT,
    usado TINYINT,
    FOREIGN KEY (cupom_id) REFERENCES cupom(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE copum_configuracao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    intervalo_atendimento INT,
    porcentagem_desconto INT
);



-- ----------------------------- TRIGGERS ------------------------
-- TRIGGERS corrigidas
DELIMITER //
CREATE TRIGGER trg_historico_agendamento
AFTER INSERT ON agendamento
FOR EACH ROW
BEGIN
    DECLARE v_taxa DECIMAL(5,2);

    SELECT taxa INTO v_taxa
    FROM pagamento
    WHERE id = NEW.pagamento_id;

    INSERT INTO historico_agendamento (
        data_horario,
        agendamento_id,
        agendamento_funcionario_id,
        agendamento_servico_id,
        agendamento_usuario_id,
        agendamento_status_agendamento_id,
        agendamento_pagamento_id,
        inicio,
        fim,
        preco,
        taxa_utilizada
    )
    VALUES (
        NOW(),
        NEW.id,
        NEW.funcionario_id,
        NEW.servico_id,
        NEW.usuario_id,
        NEW.status_agendamento_id,
        NEW.pagamento_id,
        NEW.inicio,
        NEW.fim,
        NEW.preco,
        v_taxa
    );
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_historico_agendamento_update
AFTER UPDATE ON agendamento
FOR EACH ROW
BEGIN
    DECLARE v_taxa DECIMAL(5,2);

    SELECT taxa INTO v_taxa
    FROM pagamento
    WHERE id = NEW.pagamento_id;

    INSERT INTO historico_agendamento (
        data_horario,
        agendamento_id,
        agendamento_funcionario_id,
        agendamento_servico_id,
        agendamento_usuario_id,
        agendamento_status_agendamento_id,
        agendamento_pagamento_id,
        inicio,
        fim,
        preco,
        taxa_utilizada
    )
    VALUES (
        NOW(),
        NEW.id,
        NEW.funcionario_id,
        NEW.servico_id,
        NEW.usuario_id,
        NEW.status_agendamento_id,
        NEW.pagamento_id,
        NEW.inicio,
        NEW.fim,
        NEW.preco,
        v_taxa
    );
END;
//
DELIMITER ;

-- -------------------------- VIEW ----------------------------


-- KPIS DE ATENDIMENTOS, FATURAMENTO E ATENDIMENTOS CANCELADOS

CREATE OR REPLACE VIEW vw_agendamentos_mensal AS
SELECT
    YEAR(a.data) AS ano,
    MONTH(a.data) AS mes,
    COUNT(*) AS total_atendimentos,
    SUM(CASE WHEN a.status_agendamento_id = 2 THEN 1 ELSE 0 END) AS total_cancelados,
    SUM(CASE WHEN a.status_agendamento_id <> 2 THEN a.preco ELSE 0 END) AS faturamento_total
FROM
    agendamento a
GROUP BY
    YEAR(a.data),
    MONTH(a.data)
ORDER BY
    ano,
    mes;


select * from vw_agendamentos_mensal;


-- KPI DE USUARIO CADASTRADOS

CREATE VIEW vw_cadastros_mensais_usuarios AS
SELECT
    YEAR(data_criacao) AS ano,
    MONTH(data_criacao) AS mes,
    COUNT(*) AS total_cadastros
FROM
    usuario
GROUP BY
    YEAR(data_criacao),
    MONTH(data_criacao)
ORDER BY
    ano,
    mes;

select * from  vw_cadastros_mensais_usuarios;



-- GRÁFICO QTD ATENDIMENTO POR MES

CREATE TABLE calendario_2025 (
  dia DATE PRIMARY KEY
);

-- Inserindo de 2025-01-01 até 2025-12-31
INSERT INTO calendario_2025 (dia)
SELECT DATE('2025-01-01') + INTERVAL seq DAY
FROM (
  SELECT a + b * 10 AS seq
  FROM (
    SELECT 0 a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
  ) AS unidades,
  (
    SELECT 0 b UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
    UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14
    UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19
    UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24
    UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL SELECT 29
    UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32 UNION ALL SELECT 33 UNION ALL SELECT 34
    UNION ALL SELECT 35
  ) AS dezenas
  WHERE a + b * 10 <= 364
) AS dias;

CREATE OR REPLACE VIEW atendimentos_por_dia AS
SELECT
  c.dia,
  COUNT(a.id) AS total_atendimentos
FROM calendario_2025 c
LEFT JOIN agendamento a
  ON a.data = c.dia
GROUP BY c.dia
ORDER BY c.dia;


SELECT * FROM atendimentos_por_dia;

-- GRÁFICO DE ATENDIMENTO POR SERVIÇO

CREATE OR REPLACE VIEW view_atendimentos_por_servico_mes AS
SELECT
    DATE_FORMAT(a.data, '%Y-%m') AS mes,
    s.id AS servico_id,
    s.nome AS nome_servico,
    COUNT(*) AS quantidade_atendimentos
FROM
    agendamento a
JOIN
    servico s ON a.servico_id = s.id
WHERE
    s.status = 'ATIVO'
GROUP BY
    DATE_FORMAT(a.data, '%Y-%m'),
    s.id,
    s.nome
ORDER BY
    mes, nome_servico;


    select * from view_atendimentos_por_servico_mes;







