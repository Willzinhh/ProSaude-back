package br.ufsm.spi.ProSaude.dto.fichaTreino;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaTreinoDTO {
    private Long id;
    private Long turmaId;
    private Long alunoId;
    private String nomeAluno;
    private String objetivo;
    private String exercicios;
    private String observacoes;
    private LocalDate dataAtualizacao;
}