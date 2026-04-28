ALTER TABLE inscricao
    ADD COLUMN semestre VARCHAR(10) NOT NULL; -- Ex: "2026/1"

-- Criamos uma UNIQUE CONSTRAINT composta
-- Isso permite o CPF 123 no semestre 2026/1 e no 2026/2,
-- mas PROÍBE dois registros para o mesmo CPF no mesmo semestre.
ALTER TABLE inscricao
    ADD CONSTRAINT uc_aluno_semestre UNIQUE (aluno_id, semestre);