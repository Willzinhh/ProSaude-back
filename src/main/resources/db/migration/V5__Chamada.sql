-- Tabela principal de chamadas
CREATE TABLE chamada (
                            id BIGSERIAL PRIMARY KEY,
                            turma_id BIGINT NOT NULL,
                            data DATE NOT NULL,
                            CONSTRAINT fk_chamada_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE
);

-- 1. Adiciona as novas colunas permitindo NULL temporariamente para não quebrar registros existentes
ALTER TABLE presenca
    ADD COLUMN IF NOT EXISTS chamada_id BIGINT,
    ADD COLUMN IF NOT EXISTS aluno_id BIGINT;

-- 2. Altera a coluna 'presente' (se já existir como BOOLEAN, garante o padrão DEFAULT FALSE)
ALTER TABLE presenca
    ALTER COLUMN presente SET DEFAULT FALSE;

-- 3. Adiciona as Chaves Estrangeiras (Foreign Keys) para vincular com tb_chamada e tb_aluno
ALTER TABLE presenca
    ADD CONSTRAINT fk_presenca_chamada
        FOREIGN KEY (chamada_id) REFERENCES chamada(id) ON DELETE CASCADE;

ALTER TABLE presenca
    ADD CONSTRAINT fk_presenca_aluno
        FOREIGN KEY (aluno_id) REFERENCES usuario(id) ON DELETE CASCADE;

-- Índice para acelerar a busca de chamadas por turma e data
CREATE INDEX idx_chamada_turma_data ON chamada(turma_id, data);