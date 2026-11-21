package br.ufsm.spi.ProSaude.model.fichaTreino;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ficha_treino", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"turma_id", "aluno_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    private String objetivo;

    @Column(columnDefinition = "TEXT")
    private String exercicios;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;
}