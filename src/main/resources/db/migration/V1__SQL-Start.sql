CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         perfil VARCHAR(20) NOT NULL, -- 'COORDENADOR' ou 'BOLSISTA' ou 'ALUNO'
                         primeiro_acesso BOOLEAN DEFAULT TRUE,
                         telefone VARCHAR(20),
                         telefone_emergencia VARCHAR(20),
                         CPF VARCHAR(14),
                         data_nascimento DATE,
                         observacao_medica TEXT
);

CREATE TABLE turma (
                       id SERIAL PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL,
                       vagas INTEGER,
                       descricao TEXT,
                       bolsista_responsavel_id INTEGER REFERENCES usuario(id),
                       hora_inicio TIME NOT NULL,
                       hora_fim TIME NOT NULL,
                       SEGUNDA BOOLEAN,TERCA BOOLEAN,QUARTA BOOLEAN,QUINTA BOOLEAN,SEXTA BOOLEAN,SABADO BOOLEAN,DOMINGO BOOLEAN
);


CREATE TABLE inscricao (
                           id SERIAL PRIMARY KEY,
                           aluno_id INTEGER NOT NULL,
                           turma_id INTEGER NOT NULL,
                           data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           status VARCHAR(20) DEFAULT 'ATIVO', -- Ex: ATIVO, CANCELADO, ESPERA
                           semestre VARCHAR(10) NOT NULL,
                           CONSTRAINT fk_aluno FOREIGN KEY (aluno_id) REFERENCES usuario(id),
                           CONSTRAINT fk_turma FOREIGN KEY (turma_id) REFERENCES turma(id),
                           CONSTRAINT unique_inscricao UNIQUE (aluno_id, turma_id), -- Impede inscrição duplicada,
                           CONSTRAINT uc_aluno_semestre UNIQUE (aluno_id, semestre)
);


--
--
-- CREATE TABLE presenca (
--                           id SERIAL PRIMARY KEY,
--                           inscricao_id INTEGER NOT NULL REFERENCES inscricao(id) ON DELETE CASCADE,
--                           data_hora_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                           presente BOOLEAN NOT NULL DEFAULT TRUE -- TRUE = Presente, FALSE = Falta
-- );

