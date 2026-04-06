package br.ufsm.spi.ProSaude.dto.turma;

import br.ufsm.spi.ProSaude.model.turma.DiaSemana;
import br.ufsm.spi.ProSaude.model.turma.Turma;

import java.util.List;

public record TurmaDTO(
        Long id,
        String nome,
        List<String> diasSemana // Adicione aqui

) {
    public TurmaDTO(Turma turma) {
        this(turma.getId(), turma.getNome(), turma.getDiasSemana().stream().map(Enum::name).toList());
    }
}
