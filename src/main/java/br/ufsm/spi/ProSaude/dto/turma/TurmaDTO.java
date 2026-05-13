package br.ufsm.spi.ProSaude.dto.turma;


import br.ufsm.spi.ProSaude.model.turma.Turma;

import java.util.List;

public record TurmaDTO(
        Long id,
        String nome,
        Boolean SEGUNDA,
        Boolean TERCA,
        Boolean QUARTA,
        Boolean QUINTA,
        Boolean SEXTA,
        Boolean SABADO,
        Boolean DOMINGO// Adicione aqui

) {
    public TurmaDTO(Turma turma) {
        this(turma.getId(), turma.getNome(), turma.isSEGUNDA(), turma.isTERCA(), turma.isQUARTA(), turma.isQUINTA(), turma.isSEXTA(), turma.isSABADO(), turma.isDOMINGO());
    }
}
