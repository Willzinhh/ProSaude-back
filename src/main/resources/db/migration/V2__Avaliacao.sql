CREATE TABLE avaliacao (
                           id BIGSERIAL PRIMARY KEY, -- Se for MySQL, usa: id BIGINT AUTO_INCREMENT PRIMARY KEY
                           data_avaliacao DATE NOT NULL,

    -- Chaves Estrangeiras para a tabela de Usuários
                           aluno_id BIGINT NOT NULL,
                           avaliador_id BIGINT NOT NULL,

    -- Anamnese (ANA)
                           ana_profi VARCHAR(255),
                           ana_hs_trab DOUBLE PRECISION,
                           ana_turn_trab VARCHAR(255),
                           ana_fuma BOOLEAN NOT NULL,
                           ana_fuma_tempo VARCHAR(255),
                           ana_alcool BOOLEAN NOT NULL,
                           ana_quali_sono VARCHAR(50), -- Guarda o texto do Enum
                           ana_hs_sono TIME,           -- Mapeia o LocalTime perfeitamente
                           ana_copos_agua_dia DOUBLE PRECISION,
                           ana_alimentacao VARCHAR(50), -- Guarda o texto do Enum
                           ana_ref_dia DOUBLE PRECISION,
                           ana_cirurgia VARCHAR(255),
                           ana_prob_cardiaco VARCHAR(255),

    -- Antropometria (ANT)
                           ant_peso DOUBLE PRECISION,
                           ant_altura DOUBLE PRECISION,
                           ant_imc DOUBLE PRECISION,
                           ant_imc_class VARCHAR(255),
                           ant_peri_cintura DOUBLE PRECISION,
                           ant_peri_quadril DOUBLE PRECISION,
                           ant_rcq DOUBLE PRECISION,
                           ant_rcq_class VARCHAR(255),

    -- Composição / Dores Gerais (COM)
                           com_escala_fig DOUBLE PRECISION,
                           com_escala_fig_quer DOUBLE PRECISION,
                           com_dorehj BOOLEAN NOT NULL,

    -- Dados do Sono (Embedded - Questionário de Pittsburgh)
                           q5a_demora_dormir INT,
                           q5b_acordar_noite INT,
                           q5c_banheiro_noite INT,
                           q5d_dificuldade_respirar INT,
                           q5e_tossir_roncar INT,
                           q5f_sentir_frio INT,
                           q5g_sentir_calor INT,
                           q5h_pesadelos INT,
                           q5i_sentir_dores INT,
                           q5j_outra_razao_descricao VARCHAR(255),
                           q5j_outra_razao_frequencia INT,
                           q6_classificacao_geral VARCHAR(255),
                           q7_remedio_frequencia INT,
                           q7_remedio_quais VARCHAR(255),
                           q8_ficar_acordado_atividades INT,
                           q9_falta_entusiasmo INT,
                           q9_comentarios VARCHAR(255),
                           q10_cochila BOOLEAN,
                           q10_cochila_comentarios VARCHAR(255),
                           q10_cochila_intencional BOOLEAN,
                           q10_cochila_intencional_comentarios VARCHAR(255),
                           q10_cochilar_significado VARCHAR(255),
                           q10_cochilar_significado_outro VARCHAR(255),
                           q10_cochilar_significado_comentarios VARCHAR(255),

    -- Postura - Vista Anterior
                           pos_anterior_cabeca VARCHAR(255),
                           pos_anterior_ombros VARCHAR(255),
                           pos_anterior_comp_bracos VARCHAR(255),
                           pos_anterior_triangulo_tales VARCHAR(255),
                           pos_anterior_tronco VARCHAR(255),
                           pos_anterior_linha_mamilar VARCHAR(255),
                           pos_anterior_equi_horiz_pelvico VARCHAR(255),
                           pos_anterior_cicatriz_umbilical VARCHAR(255),
                           pos_anterior_quadril_rod VARCHAR(255),
                           pos_anterior_joelhos VARCHAR(255),
                           pos_anterior_pes VARCHAR(255),

    -- Postura - Vista Perfil
                           pos_perfil_cabeca VARCHAR(255),
                           pos_perfil_ombros VARCHAR(255),
                           pos_perfil_membros_superiores VARCHAR(255),

    -- Postura - Coluna Vertebral
                           pos_coluna_cervical VARCHAR(255),
                           pos_coluna_dorsal VARCHAR(255),
                           pos_coluna_lombar VARCHAR(255),
                           pos_coluna_quadril VARCHAR(255),
                           pos_coluna_joelhos VARCHAR(255),

    -- Postura - Vista Posterior
                           pos_posterior_escoliose VARCHAR(255),
                           pos_posterior_gibosidade VARCHAR(255),
                           pos_posterior_tendao_aquiles VARCHAR(255),

    -- Observações Gerais
                           obs TEXT,

    -- Constraints de Chave Estrangeira (Altere 'usuario' para o nome real da tua tabela de usuários se for diferente)
                           CONSTRAINT fk_avaliacao_aluno FOREIGN KEY (aluno_id) REFERENCES usuario(id) ON DELETE CASCADE,
                           CONSTRAINT fk_avaliacao_avaliador FOREIGN KEY (avaliador_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE TABLE avaliacao_dores (
                                 avaliacao_id BIGINT NOT NULL,
                                 local_dor VARCHAR(50) NOT NULL,       -- Guarda a chave (ex: "A", "B", "C")
                                 intensidade_dor INT NOT NULL,         -- Guarda o valor (ex: 8)

    -- A chave primária composta garante que não existam duas intensidades para o mesmo local na mesma avaliação
                                 PRIMARY KEY (avaliacao_id, local_dor),

    -- Vinculação com a tabela pai
                                 CONSTRAINT fk_dores_avaliacao FOREIGN KEY (avaliacao_id) REFERENCES avaliacao(id) ON DELETE CASCADE
);