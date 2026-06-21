package br.ufsm.spi.ProSaude.dto.turma;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;

import java.time.LocalTime;

public record TurmaRequestDTO(
        Long id,
        String nome,
        String descricao,
        int vagas,
        String semestre,
        Usuario bolsista_responsavel,
        LocalTime horaInicio,
        LocalTime horaFim,
        Boolean SEGUNDA,
        Boolean TERCA,
        Boolean QUARTA,
        Boolean QUINTA,
        Boolean SEXTA,
        Boolean SABADO,
        Boolean DOMINGO
) {
}
