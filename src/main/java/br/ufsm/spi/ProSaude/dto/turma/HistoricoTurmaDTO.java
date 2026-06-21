package br.ufsm.spi.ProSaude.dto.turma;

public record HistoricoTurmaDTO(
        Long turmaId,
        String nomeTurma,
        String semestre,
        String status
) {}