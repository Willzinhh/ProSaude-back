-- 1. Tabela de Usuário (Coordenadores e Bolsistas)
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         permissao VARCHAR(20) NOT NULL -- 'COORDENADOR' ou 'BOLSISTA'
);

-- 2. Tabela de Aluno (Público Externo)
CREATE TABLE aluno (
                       id SERIAL PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL,
                       telefone VARCHAR(20),
                       observacaoMedica TEXT
);

-- 3. Tabela de Turma
CREATE TABLE turma (
                       id SERIAL PRIMARY KEY,
                       codigo VARCHAR (200) NOT NULL,
                       nome VARCHAR(100) NOT NULL,
                       descricao TEXT,
                       bolsista_responsavel_id INTEGER REFERENCES usuario(id), -- Corrigido para 'usuario'
                       monitor_id INTEGER REFERENCES usuario(id)               -- Corrigido para 'usuario'
);


-- 4. Tabela de Inscrição (Matrícula)
CREATE TABLE inscricao ( -- Corrigido de 'inscricoe' para 'inscricao'
                           id SERIAL PRIMARY KEY,
                           aluno_id INTEGER REFERENCES aluno(id) ON DELETE CASCADE,  -- Corrigido para 'aluno'
                           turma_id INTEGER REFERENCES turma(id) ON DELETE CASCADE,  -- Corrigido para 'turma'
                           data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabela de Presença (Registro em tempo real)
CREATE TABLE presenca (
                          id SERIAL PRIMARY KEY,
                          aluno_id INTEGER REFERENCES aluno(id),   -- Corrigido para 'aluno'
                          turma_id INTEGER REFERENCES turma(id),   -- Corrigido para 'turma'
                          bolsista_id INTEGER REFERENCES usuario(id), -- Corrigido para 'usuario'
                          data_hora_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          presente BOOLEAN DEFAULT FALSE
);