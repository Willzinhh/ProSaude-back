-- 1. Tabela de Usuário (Coordenadores e Bolsistas)
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         perfil VARCHAR(20) NOT NULL, -- 'COORDENADOR' ou 'BOLSISTA' ou 'ALUNO'
                         primeiro_acesso BOOLEAN DEFAULT TRUE
);


-- 3. Tabela de Turma
CREATE TABLE turma (
                       id SERIAL PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL,
                       descricao TEXT,
                       bolsista_responsavel_id INTEGER REFERENCES usuario(id), -- Corrigido para 'usuario'
                       hora_inicio TIME NOT NULL,
                       hora_fim TIME NOT NULL
);

CREATE TABLE turma_dias (
                            turma_id INTEGER NOT NULL REFERENCES turma(id) ON DELETE CASCADE,
                            dia VARCHAR(20) NOT NULL,
                            PRIMARY KEY (turma_id, dia) -- Garante que não haja o mesmo dia repetido para a mesma turma
);
CREATE TABLE dados_aluno (
                             usuario_id INTEGER PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
                             telefone VARCHAR(20),
                             CPF VARCHAR(14),
                             observacao_medica TEXT,
                             data_nascimento DATE
);

CREATE TABLE inscricao (
                           id SERIAL PRIMARY KEY,
                           aluno_id INTEGER NOT NULL,
                           turma_id INTEGER NOT NULL,
                           data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           status VARCHAR(20) DEFAULT 'ATIVO', -- Ex: ATIVO, CANCELADO, ESPERA
                           CONSTRAINT fk_aluno FOREIGN KEY (aluno_id) REFERENCES usuario(id),
                           CONSTRAINT fk_turma FOREIGN KEY (turma_id) REFERENCES turma(id),
                           CONSTRAINT unique_inscricao UNIQUE (aluno_id, turma_id) -- Impede inscrição duplicada
);

-- 3. Criar a tabela de Presença (Histórico)

CREATE TABLE presenca (
                          id SERIAL PRIMARY KEY,
                          inscricao_id INTEGER NOT NULL REFERENCES inscricao(id) ON DELETE CASCADE,
                          data_hora_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          presente BOOLEAN NOT NULL DEFAULT TRUE -- TRUE = Presente, FALSE = Falta
);

