package br.ufsm.spi.ProSaude.dto.inscricao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InscritoDTO {
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate dataInscricao;
    private Long faltas;
}
