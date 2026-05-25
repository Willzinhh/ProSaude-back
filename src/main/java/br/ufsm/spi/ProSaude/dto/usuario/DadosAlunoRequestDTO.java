package br.ufsm.spi.ProSaude.dto.usuario;

import java.time.LocalDate;

public record DadosAlunoRequestDTO(
        String telefone,
        String cpf,
        String observacaoMedica,
        LocalDate dataNascimento
) {
}
