package br.ufsm.spi.ProSaude.model.presenca;

import jakarta.validation.constraints.NotNull;

public record ChamadaDTO(
        Long alunoId,    // Usado se o bolsista selecionar o nome na lista do app
        String cpf,      // Usado se houver leitura de documento ou busca manual
        @NotNull Long turmaId // Obrigatório para saber onde registrar a frequência [cite: 12]
) {}
