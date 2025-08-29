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
