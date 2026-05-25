package br.ufsm.spi.ProSaude.model.inscricao;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // EM VEZ DE Long usuarioId, USE O OBJETO:
    @ManyToOne
    @JoinColumn(name = "aluno_id") // Nome da coluna no banco
    private Usuario aluno;

    // EM VEZ DE Long turmaId, USE O OBJETO:
    @ManyToOne
    @JoinColumn(name = "turma_id") // Nome da coluna no banco
    private Turma turma;

    private LocalDate dataInscricao;
    private String status;
    private String semestre;

}