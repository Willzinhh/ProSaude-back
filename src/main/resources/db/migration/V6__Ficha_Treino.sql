-- V7__Create_Ficha_Treino_And_Alter_Turma.sql
ALTER TABLE turma ADD COLUMN possui_ficha_treino BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE ficha_treino (
                              id BIGSERIAL PRIMARY KEY,
                              turma_id BIGINT NOT NULL REFERENCES turma(id) ON DELETE CASCADE,
                              aluno_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                              objetivo VARCHAR(255),
                              exercicios TEXT,
                              observacoes TEXT,
                              data_atualizacao DATE,
                              CONSTRAINT uk_ficha_turma_aluno UNIQUE (turma_id, aluno_id)
);