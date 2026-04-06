package br.ufsm.spi.ProSaude.dto.turma;

import br.ufsm.spi.ProSaude.model.turma.DiaSemana;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;

import java.time.LocalTime;
import java.util.List;

public record TurmaRequestDTO(
        Long id,
        String nome,
        String descricao,
        Usuario bolsistaResponsavel,
        LocalTime horaInicio,
        LocalTime horaFim,
        List<DiaSemana> diasSemana
) {}
