-- Tabela principal de chamadas
CREATE TABLE chamada (
                            id BIGSERIAL PRIMARY KEY,
                            turma_id BIGINT NOT NULL,
                            data DATE NOT NULL,
                            CONSTRAINT fk_chamada_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE
);

CREATE TABLE presenca (
                            id BIGSERIAL PRIMARY KEY,
                            chamada_id BIGINT NOT NULL,
                            aluno_id BIGINT NOT NULL,
                            presente BOOLEAN NOT NULL DEFAULT FALSE,
                            CONSTRAINT fk_presenca_chamada FOREIGN KEY (chamada_id) REFERENCES chamada(id) ON DELETE CASCADE,
    
    CONSTRAINT fk_presenca_aluno FOREIGN KEY (aluno_id) REFERENCES usuario(id) ON DELETE CASCADE
    );

CREATE INDEX idx_chamada_turma_data ON tb_chamada(turma_id, data);