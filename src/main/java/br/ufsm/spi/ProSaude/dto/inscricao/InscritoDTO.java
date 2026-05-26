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
    private long id;
    private String nome;
    private String telefone;
    private String telefoneEmergencia;
    private LocalDate dataInscricao;


}
